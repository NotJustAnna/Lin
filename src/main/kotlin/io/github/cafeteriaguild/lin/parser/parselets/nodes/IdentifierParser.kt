package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.access.AssignNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.ModifierHandler
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object IdentifierParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val name = token.value

        if (token.value in LinModifier.names) {
            val ahead = listOf(token) + ctx.peekAheadUntil(OBJECT, FUN, CLASS, INTERFACE, VAL, VAR)
            if (ahead.all { it.type == IDENTIFIER && it.value in LinModifier.names || it.type == NL }) {
                ctx.matchAll(IDENTIFIER)
                return ModifierHandler.handle(ctx, LinModifier.parse(ahead.filter { it.type == IDENTIFIER }))
            }
        }

        ctx.skipOnlyUntil(ASSIGN)
        if (ctx.match(ASSIGN)) {
            ctx.matchAll(NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return AssignNode(name, expr, token.section)
        }
        ctx.maybeIgnoreNL()
        return IdentifierExpr(name, token.section)
    }
}
