package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareVariableExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

class DeclareVariableParser(val mutable: Boolean) : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        val ident = ctx.eat(TokenType.IDENTIFIER)

        if (ctx.match(TokenType.ASSIGN)) {
            val expr = ctx.parseExpression()
            return DeclareVariableExpr(ident.value, mutable, expr, token.span(expr))
        }
        return DeclareVariableExpr(ident.value, mutable, null, token.span(ident))
    }
}