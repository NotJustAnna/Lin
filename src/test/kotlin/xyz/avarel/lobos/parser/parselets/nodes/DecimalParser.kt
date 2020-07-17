package xyz.avarel.lobos.parser.parselets.nodes

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.nodes.F64Expr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object DecimalParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val value = token.string.toDouble()
        return F64Expr(value, token.section)
    }
}
