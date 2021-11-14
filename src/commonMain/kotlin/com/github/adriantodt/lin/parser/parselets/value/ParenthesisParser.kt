package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object ParenthesisParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        val node = ctx.parseExpression()
        if (node !is Expr) {
            return InvalidNode {
                section(token.section)
                child(node)
                error(SyntaxException("Expected an expression", node.section))
            }
        }
        ctx.eat(TokenType.R_PAREN)
        return node
    }
}
