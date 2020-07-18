package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareVariableExpr
import io.github.cafeteriaguild.lin.ast.expr.declarations.DestructuringVariableExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

class DeclareVariableParser(val mutable: Boolean) : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        ctx.skipOnlyUntil(TokenType.L_PAREN)
        if (ctx.match(TokenType.L_PAREN)) {
            return parseDestructuring(ctx, token)
        }
        val ident = ctx.eat(TokenType.IDENTIFIER)
        ctx.skipOnlyUntil(TokenType.ASSIGN)
        if (ctx.match(TokenType.ASSIGN)) {
            ctx.matchAll(TokenType.NL)
            val last = ctx.last
            val expr = ctx.parseExpression().let {
                it as? Node ?: return InvalidExpr {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a node", it.section))
                }
            }
            return DeclareVariableExpr(ident.value, mutable, expr, token.section)
        }
        return DeclareVariableExpr(ident.value, mutable, null, token.section)
    }

    fun parseDestructuring(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
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
            it as? Node ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node", it.section))
            }
        }
        return DestructuringVariableExpr(names, mutable, expr, token.section)
    }
}