package net.notjustanna.lin.parser.parselets.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.ReturnExpr
import net.notjustanna.lin.ast.node.value.NullExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object ReturnParser : PrefixParselet<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = if (ctx.nextIsAny(TokenType.SEMICOLON, TokenType.NL)) {
            NullExpr(token.section)
        } else {
            ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
        }

        return ReturnExpr(node, token.section)
    }
}
