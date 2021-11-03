package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object ParenthesisParser : PrefixParselet<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = ctx.parseExpression()
        if (node !is Expr) {
            return InvalidNode {
                section(token.section)
                child(node)
                error(SyntaxException("Expected an expression", node.section))
            }
        }
        ctx.eat(TokenType.R_PAREN)
        return node
    }
}
