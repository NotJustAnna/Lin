package com.github.adriantodt.lin.parser.parselets.misc

import com.github.adriantodt.lin.ast.*
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.Precedence
import com.github.adriantodt.lin.parser.parselets.operations.BinaryOperatorParser
import com.github.adriantodt.tartar.api.parser.InfixParser
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

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
