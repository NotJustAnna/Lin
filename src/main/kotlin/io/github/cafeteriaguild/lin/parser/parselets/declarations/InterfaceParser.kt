package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Declaration
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareInterfaceNode
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object InterfaceParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
        val ident = ctx.eat(TokenType.IDENTIFIER)
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        if (!ctx.match(TokenType.L_BRACE)) {
            return DeclareInterfaceNode(ident.value, emptyList(), ident.section)
        }
        val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
        if (node !is MultiNode) return invalidNode(node)
        val (decl, other) = node.list.partition { it is Declaration }
        if (other.isNotEmpty()) return invalidChildNodes(node.list)
        return DeclareInterfaceNode(ident.value, decl.filterIsInstance<Declaration>(), ident.section)
    }
}