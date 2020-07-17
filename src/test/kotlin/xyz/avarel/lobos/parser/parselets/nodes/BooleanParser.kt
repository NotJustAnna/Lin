package xyz.avarel.lobos.parser.parselets.nodes

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.nodes.BooleanExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

class BooleanParser(private val value: Boolean) : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        return BooleanExpr(value, token.section)
    }
}