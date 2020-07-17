package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.declarations.UseExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.PrefixParser

object UseParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val list = mutableListOf<String>()

        do {
            list += parser.eat(TokenType.IDENT).string
        } while (parser.match(TokenType.DOUBLE_COLON))

        return UseExpr(list, token.section)
    }
}