package net.notjustanna.lin.grammar.parselets.value

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.value.FloatExpr
import net.notjustanna.lin.grammar.utils.maybeIgnoreNL
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

object FloatParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return FloatExpr(token.value.toFloat(), token.section)
    }
}
