package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareVariableNode
import io.github.cafeteriaguild.lin.ast.node.declarations.DelegatingVariableNode
import io.github.cafeteriaguild.lin.ast.node.declarations.DestructuringVariableNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

class DeclareVariableParser(val mutable: Boolean) : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.L_PAREN)
        if (ctx.match(TokenType.L_PAREN)) {
            return parseDestructuring(ctx, token)
        }
        val ident = ctx.eat(TokenType.IDENTIFIER)
        ctx.skipOnlyUntil(TokenType.ASSIGN, TokenType.IDENTIFIER)
        if (ctx.match(TokenType.ASSIGN)) {
            ctx.matchAll(TokenType.NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(it.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return DeclareVariableNode(ident.value, mutable, expr, token.section)
        } else if (ctx.match(TokenType.IDENTIFIER)) {
            val last = ctx.last
            if (last.value != "by") {
                return InvalidNode {
                    section(ctx.last.section)
                    child(DeclareVariableNode(ident.value, mutable, null, token.section))
                    error(SyntaxException("Expected 'by'", ctx.last.section))
                }
            }
            ctx.matchAll(TokenType.NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return DelegatingVariableNode(ident.value, mutable, expr, token.section)
        }
        return DeclareVariableNode(ident.value, mutable, null, token.section)
    }

    fun parseDestructuring(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val names = mutableListOf<String>()
        do {
            ctx.matchAll(TokenType.NL)
            val paramIdent = ctx.eat(TokenType.IDENTIFIER)
            ctx.matchAll(TokenType.NL)

            names += paramIdent.value
        } while (ctx.match(TokenType.COMMA))
        ctx.eat(TokenType.R_PAREN)
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.ASSIGN)
        ctx.matchAll(TokenType.NL)
        val expr = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        return DestructuringVariableNode(names, mutable, expr, token.section)
    }
}