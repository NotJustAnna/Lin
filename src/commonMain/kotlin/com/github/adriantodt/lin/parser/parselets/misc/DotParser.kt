package com.github.adriantodt.lin.parser.parselets.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.access.PropertyAccessExpr
import com.github.adriantodt.lin.ast.node.access.PropertyAssignNode
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.Precedence
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.tartar.api.grammar.InfixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.StringToken
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

class DotParser(private val nullSafe: Boolean) : InfixParselet<TokenType, Token<TokenType>, Node> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(
        ctx: ParserContext<TokenType, Token<TokenType>, Node>,
        left: Node,
        token: Token<TokenType>
    ): Node {
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
            val name = (identifier as StringToken).value

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
