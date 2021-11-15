package net.notjustanna.lin.parser.parselets.control

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.BreakExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

public object BreakParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        return BreakExpr(token.section)
    }
}
