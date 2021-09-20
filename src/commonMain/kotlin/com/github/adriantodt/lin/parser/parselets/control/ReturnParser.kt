package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.*
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object ReturnParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = if (ctx.nextIsAny(TokenType.SEMICOLON, TokenType.NL)) {
            UnitExpr(token.section)
        } else {
            ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
        }

        return ReturnExpr(node, token.section)
    }
}
