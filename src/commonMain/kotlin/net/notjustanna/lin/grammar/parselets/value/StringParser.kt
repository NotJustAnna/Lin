package net.notjustanna.lin.grammar.parselets.value

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.value.StringExpr
import net.notjustanna.lin.grammar.utils.maybeIgnoreNL
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

object StringParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return StringExpr(token.value, token.section)
    }
}
