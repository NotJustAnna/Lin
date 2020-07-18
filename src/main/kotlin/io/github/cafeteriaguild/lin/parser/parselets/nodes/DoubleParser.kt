package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.nodes.DoubleExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object DoubleParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        return DoubleExpr(token.value.toDouble(), token.section)
    }
}