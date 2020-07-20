package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareClassNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object ClassParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
        val ident = ctx.eat(TokenType.IDENTIFIER)
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        if (!ctx.match(TokenType.L_BRACE)) {
            return DeclareClassNode(ident.value, emptyList(), ident.section)
        }
        val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
        if (node !is MultiNode) return invalidNode(node)
        val (decl, other) = node.list.remapInitializers().partition { it is Declaration }
        if (other.isNotEmpty()) return invalidChildNodes(node.list)
        return DeclareClassNode(ident.value, decl.filterIsInstance<Declaration>(), ident.section)
    }
}