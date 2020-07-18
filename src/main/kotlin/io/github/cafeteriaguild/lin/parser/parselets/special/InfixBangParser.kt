package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperationType
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.parselets.BinaryOperatorParser

object InfixBangParser : InfixParser<TokenType, Node> {
    override val precedence = Precedence.NAMED_CHECKS

    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        val target = if (ctx.match(TokenType.IN)) {
            BinaryOperatorParser(Precedence.NAMED_CHECKS, BinaryOperationType.IN).parse(ctx, left, ctx.last).let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a node", it.section))
                }
            }
        } else {
            return InvalidNode {
                section(token.section)
                error(SyntaxException("Expected 'in' but got ${ctx.peek()}", token.section))
            }
        }

        return UnaryOperation(target, UnaryOperationType.NOT, token.section)
    }
}