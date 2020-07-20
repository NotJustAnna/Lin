package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.InitializerNode
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.ast.node.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

abstract class TypeParser : PrefixParser<TokenType, Node> {
    fun ParserContext<TokenType, Node>.parseBlockNode() = parseBlock(smartToExpr = false, braceConsumed = true)

    protected fun noBlockParsed(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>) = InvalidNode {
        section(token.section)
        error(SyntaxException("Couldn't parse object's block, found ${ctx.peek()}", token.section))
    }

    protected fun invalidNode(node: Node) = InvalidNode {
        child(node)
        error(SyntaxException("Invalid node found", node.section))
    }

    protected fun invalidChildNodes(list: List<Node>) = InvalidNode {
        section(list.first().section)
        list.forEach { child(it as? Declaration ?: nodeIsNotDeclaration(it)) }
    }

    private fun nodeIsNotDeclaration(node: Node) = InvalidNode {
        child(node)
        error(SyntaxException("Node is not a declaration", node.section))
    }

    fun List<Node>.remapInitializers() = map {
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
}