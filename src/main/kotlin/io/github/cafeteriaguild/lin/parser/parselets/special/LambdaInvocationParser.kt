package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence

object LambdaInvocationParser : InfixParser<TokenType, Node> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        if (left !is Expr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node", left.section))
            }
        }
        ctx.back()
        val expr = ctx.parseExpression().let {
            it as? LambdaExpr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a lambda", it.section))
            }
        }

        if (left is PropertyAccessExpr) {
            return InvokeMemberExpr(left.target, left.nullSafe, left.name, listOf(expr), token.section)
        } else if (left is IdentifierExpr) {
            return InvokeLocalExpr(left.name, listOf(expr), token.section)
        }

        return InvokeExpr(left, listOf(expr), token.section)
    }
}