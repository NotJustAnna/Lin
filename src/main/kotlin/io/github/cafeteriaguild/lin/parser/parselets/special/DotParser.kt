package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.node.access.PropertyAssignNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.Precedence
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

class DotParser(val nullSafe: Boolean) : InfixParser<TokenType, Node> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(ctx: ParserContext<TokenType, Node>, left: Node, token: Token<TokenType>): Node {
        if (left !is Expr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected an expression", left.section))
            }
        }

        ctx.matchAll(TokenType.NL)
        val identifier = ctx.eat()
        if (identifier.type == TokenType.IDENTIFIER) {
            val name = identifier.value

            // TODO implement all the op-assign (plusAssign, etc)
            return if (ctx.match(TokenType.ASSIGN)) {
                val value = ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }
                ctx.maybeIgnoreNL()
                PropertyAssignNode(left, nullSafe, name, value, token.section)
            } else {
                ctx.maybeIgnoreNL()
                PropertyAccessExpr(left, nullSafe, name, token.section)
            }
        }
        return InvalidNode {
            section(token.section)
            child(left)
            error(SyntaxException("Expected an indentifier, but found ${token.type}", identifier.section))
        }
    }
}