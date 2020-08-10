package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.AccessExpr
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareObjectNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.ast.node.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object ObjectParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER, TokenType.L_BRACE)
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) else null
        val implements = mutableListOf<AccessExpr>()
        ctx.skipOnlyUntil(TokenType.L_BRACKET)
        if (ctx.match(TokenType.L_BRACKET)) {
            do {
                ctx.matchAll(TokenType.NL)
                val target = ctx.parseExpression().let {
                    it as? AccessExpr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an accessor", it.section))
                    }
                }
                implements += target
                ctx.skipOnlyUntil(TokenType.COMMA)
            } while (ctx.match(TokenType.COMMA))
            ctx.skipOnlyUntil(TokenType.R_BRACKET)
            ctx.eat(TokenType.R_BRACKET)
        }
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        val decl = if (ctx.match(TokenType.L_BRACE)) {
            val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
            if (node !is MultiNode) return invalidNode(node)
            val (decl, other) = node.list.remapInitializers().partition { it is Declaration }
            if (other.isNotEmpty()) return invalidChildNodes(node.list)
            decl.filterIsInstance<Declaration>()
        } else emptyList()

        if (ident == null) {
            ctx.maybeIgnoreNL()
        }
        return if (ident != null) DeclareObjectNode(ident.value, implements, decl, ident.section) else ObjectExpr(implements, decl, token.section)
    }
}