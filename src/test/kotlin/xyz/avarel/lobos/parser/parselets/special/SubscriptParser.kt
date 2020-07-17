package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.access.SubscriptAccessExpr
import xyz.avarel.lobos.ast.expr.access.SubscriptAssignExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.InfixParser
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.Precedence

object SubscriptParser : InfixParser {
    override val precedence: Int get() = Precedence.DOT

    override fun parse(parser: Parser, token: Token, left: Expr): Expr {
        val index = parser.parseExpr()

        val rBracket = parser.eat(TokenType.R_BRACKET)

        return if (parser.match(TokenType.ASSIGN)) {
            val value = parser.parseExpr()
            SubscriptAssignExpr(left, index, value, left.span(value))
        } else {
            SubscriptAccessExpr(left, index, left.span(rBracket))
        }
    }
}