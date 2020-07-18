package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence

object InvocationParser : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        val arguments = mutableListOf<Expr>()

        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                arguments += ctx.parseExpression()
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_PAREN)
        }

        val rParen = ctx.last

        val position = left.span(rParen)

        if (left is PropertyAccessExpr) {
            return InvokeMemberExpr(left.target, left.name, arguments, position)
        } else if (left is IdentifierExpr) {
            return InvokeLocalExpr(left.name, arguments, position)
        }

        return InvokeExpr(left, arguments, position)
    }
}