package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.IfExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser
import xyz.avarel.lobos.parser.parseBlock

object IfParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val condition = parser.parseExpr()
        val thenBranch = parser.parseBlock()

        val elseBranch = if (parser.match(TokenType.ELSE)) {
            if (parser.nextIs(TokenType.IF)) parser.parseExpr()
            else parser.parseBlock()
        } else {
            null
        }

        return IfExpr(condition, thenBranch, elseBranch, token.section)
    }
}