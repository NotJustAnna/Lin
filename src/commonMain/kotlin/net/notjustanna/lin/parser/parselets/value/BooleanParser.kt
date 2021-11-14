package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.value.BooleanExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

class BooleanParser(val value: Boolean) : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return BooleanExpr(value, token.section)
    }
}
