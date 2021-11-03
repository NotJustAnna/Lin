package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.ReturnExpr
import com.github.adriantodt.lin.ast.node.value.NullExpr
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object ReturnParser : PrefixParselet<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = if (ctx.nextIsAny(TokenType.SEMICOLON, TokenType.NL)) {
            NullExpr(token.section)
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
