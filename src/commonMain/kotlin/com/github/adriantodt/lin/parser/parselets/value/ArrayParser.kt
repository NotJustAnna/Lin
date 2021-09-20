package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.ArrayExpr
import com.github.adriantodt.lin.ast.Expr
import com.github.adriantodt.lin.ast.InvalidNode
import com.github.adriantodt.lin.ast.Node
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object ArrayParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
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
