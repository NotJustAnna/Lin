package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.WhileExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

object WhileParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)
        val condition = ctx.parseExpression().let {
            it as? Node ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node but got a statement instead.", it.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)
        val expr = ctx.parseBlock()
        return WhileExpr(condition, expr, token.section)
    }
}