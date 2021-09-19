package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.BooleanExpr
import net.notjustanna.lin.ast.Node
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

class BooleanParser(val value: Boolean) : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return BooleanExpr(value, token.section)
    }
}
