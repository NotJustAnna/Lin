package com.github.adriantodt.lin.parser.parselets.operations

import com.github.adriantodt.lin.ast.*
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.Precedence
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

class UnaryOperatorParser(private val operator: UnaryOperationType) : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val target = ctx.parseExpression(Precedence.PREFIX).let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return UnaryOperation(target, operator, token.section)
    }
}
