package com.github.adriantodt.lin.grammar.parselets.value

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.value.ThisExpr
import com.github.adriantodt.lin.grammar.utils.maybeIgnoreNL
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.Token

object ThisParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return ThisExpr(token.section)
    }
}
