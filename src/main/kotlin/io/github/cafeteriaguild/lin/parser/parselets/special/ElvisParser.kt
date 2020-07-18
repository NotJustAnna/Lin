package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ElvisNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object ElvisParser : InfixParser<TokenType, Expr> {
    override val precedence: Int = Precedence.ELVIS

    override fun parse(ctx: ParserContext<TokenType, Expr>, left: Expr, token: Token<TokenType>): Expr {
        if (left !is Node) {
            return InvalidExpr {
                section(token.section)
                child(left)
                error(SyntaxException("Expected a node but got a statement instead.", left.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        val right = ctx.parseExpression(precedence).let {
            it as? Node ?: return InvalidExpr {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node but got a statement instead.", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return ElvisNode(left, right, token.section)
    }
}