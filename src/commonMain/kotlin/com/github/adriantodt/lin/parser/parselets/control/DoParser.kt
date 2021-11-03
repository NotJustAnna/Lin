package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.DoWhileNode
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.lin.parser.utils.parseBlock
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object DoParser : PrefixParselet<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val expr = if (ctx.match(TokenType.WHILE)) null else ctx.parseBlock() ?: ctx.parseExpression()
        ctx.matchAll(TokenType.NL)
        if (expr != null && !ctx.match(TokenType.WHILE)) return expr
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
        return DoWhileNode(expr, condition, token.section)
    }
}
