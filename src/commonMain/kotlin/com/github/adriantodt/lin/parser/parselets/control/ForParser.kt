package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.Expr
import com.github.adriantodt.lin.ast.ForNode
import com.github.adriantodt.lin.ast.InvalidNode
import com.github.adriantodt.lin.ast.Node
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.parseBlock
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object ForParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)

        val variableName = ctx.eat(TokenType.IDENTIFIER).value
        // Destructuring is implemented here

        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.IN)
        ctx.matchAll(TokenType.NL)
        val iterable = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)
        ctx.matchAll(TokenType.NL)
        val expr = ctx.parseBlock() ?: ctx.parseExpression()

        return ForNode(variableName, iterable, expr, token.section)
    }
}
