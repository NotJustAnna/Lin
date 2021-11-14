package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.BreakExpr
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

object BreakParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        return BreakExpr(token.section)
    }
}
