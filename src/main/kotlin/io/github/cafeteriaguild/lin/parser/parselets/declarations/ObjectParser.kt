package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Declaration
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareObjectNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.InitializerNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

object ObjectParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) else null
        ctx.matchAll(TokenType.NL)
        val list = (ctx.parseBlock(false) ?: return InvalidNode {
            section(token.section)
            error(SyntaxException("Couldn't parse object's block, found ${ctx.peek()}", token.section))
        }).let {
            it as? MultiNode ?: return InvalidNode {
                child(it)
                error(SyntaxException("Invalid node found", token.section))
            }
        }.list.map {
            if (it is InvokeLocalExpr) {
                val arguments = it.arguments

                if (arguments.singleOrNull() is LambdaExpr) {
                    val lambda = arguments.single() as LambdaExpr

                    if (lambda.parameters.isEmpty()) {
                        val body = lambda.body
                        InitializerNode(
                            body as? MultiNode ?: (body as MultiExpr).let { expr ->
                                MultiNode(expr.list + expr.last, expr.section)
                            },
                            body.section
                        )
                    } else it
                } else it
            } else it
        }

        if (list.any { it !is Declaration }) return InvalidNode {
            section(list.first().section)
            list.forEach {
                child(
                    if (it is Declaration) InvalidNode {
                        child(it)
                        error(SyntaxException("Node is not a declaration", token.section))
                    } else it
                )
            }
        }

        ctx.maybeIgnoreNL()
        val obj = ObjectExpr(list.filterIsInstance<Declaration>(), token.section)

        return if (ident != null) DeclareObjectNode(ident.value, obj, ident.section) else obj
    }
}