package io.github.cafeteriaguild.lin.parser.parselets

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class BinaryOperatorParser(
    override val precedence: Int,
    private val operator: BinaryOperationType,
    private val leftAssoc: Boolean = true
) : InfixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        if (left !is Node) {
            return InvalidExpr {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node", left.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        val right = ctx.parseExpression(precedence - if (leftAssoc) 0 else 1).let {
            it as? Node ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return BinaryOperation(left, right, operator, token.section)
    }
}