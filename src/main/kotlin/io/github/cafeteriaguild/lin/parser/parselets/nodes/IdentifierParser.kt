package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.AssignExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object IdentifierParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        val name = token.value

        if (ctx.match(TokenType.ASSIGN)) {
            val expr = ctx.parseExpression()

            return AssignExpr(name, expr, token.section)
        }

        return IdentifierExpr(name, token.section)

    }
}