package io.github.cafeteriaguild.lin.parser.parselets

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.AccessNode
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.ops.PreAssignUnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryAssignOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class PreAssignUnaryOperatorParser(private val operator: UnaryAssignOperationType) : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        ctx.matchAll(TokenType.NL)
        val target = ctx.parseExpression(Precedence.PREFIX).let {
            it as? AccessNode ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an accessor", it.section))
            }
        }

        ctx.maybeIgnoreNL()
        return PreAssignUnaryOperation(target, operator, token.section)
    }
}