package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.TupleExpr
import xyz.avarel.lobos.ast.expr.nodes.ReturnExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object ReturnParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val expr = if (parser.matchAny(TokenType.NL, TokenType.SEMICOLON)) {
            TupleExpr(token.section)
        } else {
            parser.parseExpr()
        }

        return ReturnExpr(expr, token.span(expr))
    }
}