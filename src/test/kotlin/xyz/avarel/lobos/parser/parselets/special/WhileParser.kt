package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.WhileExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser
import xyz.avarel.lobos.parser.parseBlock

object WhileParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val condition = parser.parseExpr()
        val body = parser.parseBlock()

        return WhileExpr(condition, body, token.section)
    }
}