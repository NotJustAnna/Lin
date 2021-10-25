package net.notjustanna.lin.grammar.parselets.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.misc.BinaryOperationType
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.ast.node.misc.UnaryOperationType
import net.notjustanna.lin.grammar.parselets.operations.BinaryOperatorParser
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.Precedence
import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object InfixBangParser : InfixParser<TokenType, Node> {
    override val precedence = Precedence.NAMED_CHECKS

    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        val target = if (ctx.match(TokenType.IN)) {
            BinaryOperatorParser(Precedence.NAMED_CHECKS, BinaryOperationType.IN).parse(ctx, left, ctx.last).let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
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
