package com.github.adriantodt.lin.parser.parselets.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.IfExpr
import com.github.adriantodt.lin.ast.node.control.IfNode
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.lin.parser.utils.parseBlock
import com.github.adriantodt.lin.parser.utils.skipOnlyUntil
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

public object IfParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.L_PAREN)
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
        ctx.matchAll(TokenType.NL)
        val thenBranch = ctx.parseBlock() ?: ctx.parseExpression()

        ctx.skipOnlyUntil(TokenType.ELSE)
        val elseBranch = if (ctx.match(TokenType.ELSE)) {
            ctx.matchAll(TokenType.NL)
            if (ctx.nextIs(TokenType.IF)) {
                ctx.parseExpression()
            } else {
                ctx.parseBlock() ?: ctx.parseExpression()
            }
        } else {
            null
        }

        if (thenBranch is Expr && elseBranch is Expr) {
            return IfExpr(condition, thenBranch, elseBranch, token.section)
        }
        return IfNode(condition, thenBranch, elseBranch, token.section)
    }
}
