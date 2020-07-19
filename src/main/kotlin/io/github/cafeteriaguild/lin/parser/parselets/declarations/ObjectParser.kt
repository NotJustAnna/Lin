package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Declaration
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareObjectNode
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object ObjectParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER, TokenType.L_BRACE)
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) else null
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
        val obj = ObjectExpr(decl, token.section)
        return if (ident != null) DeclareObjectNode(ident.value, obj, ident.section) else obj
    }
}