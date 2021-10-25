package net.notjustanna.lin.grammar.parselets.operations

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.misc.BinaryOperation
import net.notjustanna.lin.ast.node.misc.BinaryOperationType
import net.notjustanna.lin.grammar.utils.maybeIgnoreNL
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

class BinaryOperatorParser(
    override val precedence: Int,
    private val operator: BinaryOperationType,
    private val leftAssoc: Boolean = true
) : InfixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        if (left !is Expr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected an expression", left.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        val right = ctx.parseExpression(precedence - if (leftAssoc) 0 else 1).let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return BinaryOperation(left, right, operator, token.section)
    }
}
