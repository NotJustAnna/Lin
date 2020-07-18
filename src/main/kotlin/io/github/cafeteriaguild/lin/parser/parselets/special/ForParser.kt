package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.ForExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

object ForParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)
        val identifier = ctx.eat(TokenType.IDENTIFIER)
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.IN)
        ctx.matchAll(TokenType.NL)
        val iterable = ctx.parseExpression().let {
            it as? Node ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node", it.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)
        ctx.matchAll(TokenType.NL)
        val expr = ctx.parseBlock() ?: ctx.parseExpression()

        return ForExpr(identifier.value, iterable, expr, token.section)
    }
}