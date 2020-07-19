package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.AssignNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareObjectNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object IdentifierParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val name = token.value

        if (token.value in LinModifier.names) {
            val ahead = listOf(token) + ctx.peekAheadUntil(OBJECT, FUN, CLASS, INTERFACE, VAL, VAR)
            if (ahead.all { it.type == IDENTIFIER && it.value in LinModifier.names }) {
                return applyModifiers(ctx.parseExpression(), LinModifier.parse(ahead))
            }
        }

        if (ctx.match(ASSIGN)) {
            val expr = ctx.parseExpression()

            return AssignNode(name, expr, token.section)
        }
        ctx.maybeIgnoreNL()
        return IdentifierExpr(name, token.section)
    }

    private fun applyModifiers(node: Node, map: Map<LinModifier, Token<TokenType>>): Node {
        when (node) {
            is DeclareObjectNode -> {
                val companionToken = map[LinModifier.COMPANION]
                if (companionToken != null) {
                    return DeclareObjectNode(node.name, node.obj, companionToken.section, true)
                }
            }
            is ObjectExpr -> {
                val companionToken = map[LinModifier.COMPANION]
                if (companionToken != null) {
                    return DeclareObjectNode("Companion", node, companionToken.section, true)
                }
            }
        }
        return node
    }
}
