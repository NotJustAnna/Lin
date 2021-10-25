package net.notjustanna.lin.grammar.parselets.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.misc.TypeofExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object TypeofParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val node = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }

        return TypeofExpr(node, token.section)
    }
}
