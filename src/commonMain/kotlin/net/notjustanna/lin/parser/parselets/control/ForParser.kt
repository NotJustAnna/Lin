package net.notjustanna.lin.parser.parselets.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.ForNode
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.matchAll
import net.notjustanna.lin.parser.utils.parseBlock
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.StringToken
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object ForParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)

        val variableName = (ctx.eat(TokenType.IDENTIFIER) as StringToken).value
        // Destructuring is implemented here

        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.IN)
        ctx.matchAll(TokenType.NL)
        val iterable = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)
        ctx.matchAll(TokenType.NL)
        val expr = ctx.parseBlock() ?: ctx.parseExpression()

        return ForNode(variableName, iterable, expr, token.section)
    }
}
