package com.github.adriantodt.lin.grammar.parselets.control

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.ContinueExpr
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.Token

object ContinueParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        return ContinueExpr(token.section)
    }
}
