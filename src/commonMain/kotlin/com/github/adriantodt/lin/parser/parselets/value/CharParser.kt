package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.CharExpr
import com.github.adriantodt.lin.ast.InvalidNode
import com.github.adriantodt.lin.ast.Node
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object CharParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        val value = token.value
        if (value.length > 1) {
            return InvalidNode {
                section(token.section)
                error(SyntaxException("Too many characters in a character literal", token.section))
            }
        }
        return CharExpr(value.first(), token.section)
    }
}
