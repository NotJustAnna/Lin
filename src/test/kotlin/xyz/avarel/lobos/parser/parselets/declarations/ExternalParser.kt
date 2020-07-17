package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object ExternalParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        return parser.parseExpr(modifiers = modifiers + Modifier.EXTERNAL)
    }
}