package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.Expr
import com.github.adriantodt.lin.ast.InvalidNode
import com.github.adriantodt.lin.ast.Node
import com.github.adriantodt.lin.ast.WhileNode
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.parseBlock
import com.github.adriantodt.lin.parser.utils.skipOnlyUntil
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object WhileParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)
        val condition = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }

        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)

        ctx.skipOnlyUntil()

        ctx.matchAll(TokenType.NL)
        val expr = if (ctx.match(TokenType.SEMICOLON)) null else ctx.parseBlock() ?: ctx.parseExpression()
        return WhileNode(condition, expr, token.section)
    }
}
