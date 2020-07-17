package io.github.cafeteriaguild.lin.parser.grammar.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.nodes.NullExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object NullParser : PrefixParser<TokenType, Expr> {
    override fun parse(parser: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        return NullExpr(token.section)
    }
}