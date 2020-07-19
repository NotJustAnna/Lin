package io.github.cafeteriaguild.lin.parser.parselets

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.AccessExpr
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.expr.ops.AssignOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.AssignOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class AssignOperatorParser(private val operator: AssignOperationType) : InfixParser<TokenType, Node> {
    override val precedence: Int = Precedence.ASSIGNMENT
    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        if (left !is AccessExpr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected an accessor", left.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        val right = ctx.parseExpression(precedence).let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return AssignOperation(left, right, operator, token.section)
    }
}