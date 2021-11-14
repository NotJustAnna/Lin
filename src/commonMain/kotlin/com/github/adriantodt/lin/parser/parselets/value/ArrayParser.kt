package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.value.ArrayExpr
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object ArrayParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        val contents = mutableListOf<Expr>()

        ctx.matchAll(TokenType.NL)
        if (!ctx.match(TokenType.R_BRACKET)) {
            do {
                ctx.matchAll(TokenType.NL)
                contents += ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }
                ctx.matchAll(TokenType.NL)
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_BRACKET)
        }
        return ArrayExpr(contents, token.section)
    }
}
