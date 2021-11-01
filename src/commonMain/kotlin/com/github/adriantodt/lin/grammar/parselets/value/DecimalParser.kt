package com.github.adriantodt.lin.grammar.parselets.value

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.value.DecimalExpr
import com.github.adriantodt.lin.grammar.utils.maybeIgnoreNL
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.Token

object DecimalParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return DecimalExpr(token.value.toDouble(), token.section)
    }
}
