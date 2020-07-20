package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.ast.node.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object InvocationParser : InfixParser<TokenType, Node> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        if (left !is Expr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected an expression", left.section))
            }
        }
        val arguments = mutableListOf<Expr>()

        ctx.matchAll(TokenType.NL)
        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                ctx.matchAll(TokenType.NL)
                arguments += ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }
                ctx.matchAll(TokenType.NL)
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_PAREN)
        }
        if (ctx.nextIs(TokenType.L_BRACE)) {
            arguments += ctx.parseExpression().let {
                it as? LambdaExpr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a lambda", it.section))
                }
            }
        }

        ctx.maybeIgnoreNL()

        if (left is PropertyAccessExpr) {
            return InvokeMemberExpr(left.target, left.nullSafe, left.name, arguments, token.section)
        } else if (left is IdentifierExpr) {
            return InvokeLocalExpr(left.name, arguments, token.section)
        }

        return InvokeExpr(left, arguments, token.section)
    }
}