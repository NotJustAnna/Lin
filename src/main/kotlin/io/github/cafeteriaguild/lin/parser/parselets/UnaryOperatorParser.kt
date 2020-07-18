package io.github.cafeteriaguild.lin.parser.parselets

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class UnaryOperatorParser(private val operator: UnaryOperationType) : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        val target = ctx.parseExpression(Precedence.PREFIX)
        ctx.maybeIgnoreNL()
        return UnaryOperation(target, operator, token.span(target))
    }
}