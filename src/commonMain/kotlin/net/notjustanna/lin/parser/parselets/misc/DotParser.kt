package net.notjustanna.lin.parser.parselets.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.access.PropertyAccessExpr
import net.notjustanna.lin.ast.node.access.PropertyAssignNode
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.Precedence
import net.notjustanna.lin.parser.utils.matchAll
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

class DotParser(private val nullSafe: Boolean) : InfixParselet<TokenType, Node> {
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
