package com.github.adriantodt.lin.parser.parselets.misc

import com.github.adriantodt.lin.ast.*
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.lin.parser.utils.skipOnlyUntil
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object IdentifierParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val name = token.value

        // TODO Implement `operator` modifier for functions (Yes it is implemented here)

        ctx.skipOnlyUntil(TokenType.ASSIGN)
        if (ctx.match(TokenType.ASSIGN)) {
            // TODO Implement assign operations

            // TODO Implement increment/decrement

            ctx.matchAll(TokenType.NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return AssignNode(name, expr, token.section)
        }
        ctx.maybeIgnoreNL()
        return IdentifierExpr(name, token.section)
    }
}
