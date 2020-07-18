package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessNode
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAssignExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class DotParser(val nullSafe: Boolean) : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        if (left !is Node) {
            return InvalidExpr {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node but got a statement instead.", left.section))
            }
        }

        ctx.matchAll(TokenType.NL)
        val identifier = ctx.eat()
        if (identifier.type == TokenType.IDENTIFIER) {
            val name = identifier.value

            // TODO implement all the op-assign (plusAssign, etc)
            return if (ctx.match(TokenType.ASSIGN)) {
                val value = ctx.parseExpression().let {
                    it as? Node ?: return InvalidExpr {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected a node but got a statement instead.", it.section))
                    }
                }
                ctx.maybeIgnoreNL()
                PropertyAssignExpr(left, nullSafe, name, value, token.section)
            } else {
                ctx.maybeIgnoreNL()
                PropertyAccessNode(left, nullSafe, name, token.section)
            }
        }
        return InvalidExpr {
            section(token.section)
            child(left)
            error(SyntaxException("Expected an indentifier, but found ${token.type}", identifier.section))
        }
    }
}