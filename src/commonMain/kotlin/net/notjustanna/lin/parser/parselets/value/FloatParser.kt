package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.FloatExpr
import net.notjustanna.lin.ast.Node
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

object FloatParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return FloatExpr(token.value.toFloat(), token.section)
    }
}
