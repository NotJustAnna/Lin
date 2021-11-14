package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.value.DecimalExpr
import com.github.adriantodt.lin.lexer.DoubleToken
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

object DecimalParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return DecimalExpr((token as DoubleToken).value, token.section)
    }
}
