package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.SubscriptAccessNode
import io.github.cafeteriaguild.lin.ast.expr.access.SubscriptAssignExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object SubscriptParser : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        if (left !is Node) {
            return InvalidExpr {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node", left.section))
            }
        }
        val arguments = mutableListOf<Node>()

        if (!ctx.match(TokenType.R_BRACKET)) {
            do {
                arguments += ctx.parseExpression().let {
                    it as? Node ?: return InvalidExpr {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected a node", it.section))
                    }
                }
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_BRACKET)
        }

        val rBracket = ctx.last

        // TODO implement all the op-assign (plusAssign, etc)
        // TL;DR "a[b] += c" is completely valid...
        return if (ctx.match(TokenType.ASSIGN)) {
            val value = ctx.parseExpression().let {
                it as? Node ?: return InvalidExpr {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a node", it.section))
                }
            }
            ctx.maybeIgnoreNL()
            SubscriptAssignExpr(left, arguments, value, left.span(value))
        } else {
            ctx.maybeIgnoreNL()
            SubscriptAccessNode(left, arguments, left.span(rBracket))
        }
    }
}