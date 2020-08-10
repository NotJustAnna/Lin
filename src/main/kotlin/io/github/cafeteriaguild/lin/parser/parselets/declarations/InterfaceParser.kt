package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.AccessExpr
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareInterfaceNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object InterfaceParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
        val ident = ctx.eat(TokenType.IDENTIFIER)
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
        if (!ctx.match(TokenType.L_BRACE)) {
            return DeclareInterfaceNode(ident.value, implements, emptyList(), ident.section)
        }
        val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
        if (node !is MultiNode) return invalidNode(node)
        val (decl, other) = node.list.partition { it is Declaration }
        if (other.isNotEmpty()) return invalidChildNodes(node.list)
        return DeclareInterfaceNode(ident.value, implements, decl.filterIsInstance<Declaration>(), ident.section)
    }
}