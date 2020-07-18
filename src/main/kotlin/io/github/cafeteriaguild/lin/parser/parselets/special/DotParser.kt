package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAssignExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence

object DotParser : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        val identifier = ctx.eat()
        if (identifier.type == TokenType.IDENTIFIER) {
            val name = identifier.value

            // TODO implement all the op-assign (plusAssign, etc)
            return if (ctx.match(TokenType.ASSIGN)) {
                val value = ctx.parseExpression()
                PropertyAssignExpr(left, name, value, left.span(value))
            } else {
                PropertyAccessExpr(left, name, left.span(identifier))
            }
        }
        throw SyntaxException("Invalid identifier", identifier.section)
    }
}