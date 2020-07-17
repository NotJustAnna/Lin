package xyz.avarel.lobos.parser.parselets.nodes

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.nodes.StringExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object StringParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val value = token.string
        return StringExpr(value, token.section)
    }
}
