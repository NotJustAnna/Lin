package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.ListLiteralExpr
import xyz.avarel.lobos.ast.expr.misc.MapLiteralExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object CollectionParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        if (parser.match(TokenType.R_BRACKET)) { // []
            return ListLiteralExpr(emptyList(), token.span(parser.last))
        }
        if (parser.match(TokenType.COLON)) { // [:]
            parser.eat(TokenType.R_BRACKET)
            return MapLiteralExpr(emptyMap(), token.span(parser.last))
        }

        val expr = parser.parseExpr()

        if (parser.match(TokenType.COLON)) {
            val exprValue = parser.parseExpr()
            val map = mutableMapOf<Expr, Expr>()
            map[expr] = exprValue

            while (parser.match(TokenType.COMMA)) {
                val key = parser.parseExpr()
                parser.eat(TokenType.COLON)
                val value = parser.parseExpr()
                map[key] = value
            }

            parser.eat(TokenType.R_BRACKET)

            return MapLiteralExpr(map, token.span(parser.last))
        }

        val list = mutableListOf(expr)

        while (parser.match(TokenType.COMMA)) {
            list += parser.parseExpr()
        }

        parser.eat(TokenType.R_BRACKET)

        return ListLiteralExpr(list, token.span(parser.last))
    }
}