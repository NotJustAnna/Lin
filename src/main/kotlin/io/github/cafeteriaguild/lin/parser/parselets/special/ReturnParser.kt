package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ReturnExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object ReturnParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        val expr = if (ctx.matchAny(TokenType.NL, TokenType.SEMICOLON)) {
            UnitExpr(token.section)
        } else {
            ctx.parseExpression()
        }

        return ReturnExpr(expr, token.span(expr))
    }
}