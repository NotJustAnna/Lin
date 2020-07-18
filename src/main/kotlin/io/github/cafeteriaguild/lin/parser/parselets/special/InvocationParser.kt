package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeNode
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object InvocationParser : InfixParser<TokenType, Expr> {
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

        ctx.matchAll(TokenType.NL)
        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                ctx.matchAll(TokenType.NL)
                arguments += ctx.parseExpression().let {
                    it as? Node ?: return InvalidExpr {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected a node", it.section))
                    }
                }
                ctx.matchAll(TokenType.NL)
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_PAREN)
        }

        ctx.maybeIgnoreNL()

        if (left is PropertyAccessNode) {
            return InvokeMemberNode(left.target, left.nullSafe, left.name, arguments, token.section)
        } else if (left is IdentifierNode) {
            return InvokeLocalNode(left.name, arguments, token.section)
        }

        return InvokeNode(left, arguments, token.section)
    }
}