package net.notjustanna.lin.parser.parselets.control

import net.notjustanna.lin.ast.Expr
import net.notjustanna.lin.ast.InvalidNode
import net.notjustanna.lin.ast.Node
import net.notjustanna.lin.ast.ThrowExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object ThrowParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }

        return ThrowExpr(node, token.section)
    }
}
