package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IntExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object IntParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        return IntExpr(token.value.toInt(), token.section)
    }
}