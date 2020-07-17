package xyz.avarel.lobos.parser.parselets.nodes

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.access.AssignExpr
import xyz.avarel.lobos.ast.expr.nodes.IdentExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object IdentParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val name = token.string

        if (parser.match(TokenType.ASSIGN)) {
            val expr = parser.parseExpr()

            return AssignExpr(name, expr, token.section)
        }

        return IdentExpr(name, token.section)

    }
}
