package io.github.cafeteriaguild.lin.parser.parselets

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.ops.PostAssignUnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryAssignOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class PostAssignUnaryOperatorParser(private val operator: UnaryAssignOperationType) : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.POSTFIX
    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        if (left !is Node) {
            return InvalidExpr {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node but got a statement instead", left.section))
            }
        }
        ctx.maybeIgnoreNL()
        return PostAssignUnaryOperation(left, operator, token.section)
    }
}