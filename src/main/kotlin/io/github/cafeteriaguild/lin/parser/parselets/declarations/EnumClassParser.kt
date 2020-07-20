package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareEnumClassNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object EnumClassParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
        val ident = ctx.eat(TokenType.IDENTIFIER)

        //TODO Enum class primary constructor parameters
        ctx.skipOnlyUntil(TokenType.R_PAREN)
        if (ctx.match(TokenType.R_PAREN)) {
            ctx.skipOnlyUntil(TokenType.L_PAREN)
            ctx.eat(TokenType.L_PAREN)
        }

        ctx.skipOnlyUntil(TokenType.L_BRACE)
        if (!ctx.match(TokenType.L_BRACE)) {
            return DeclareEnumClassNode(ident.value, emptyList(), emptyList(), ident.section)
        }
        ctx.skipOnlyUntil(TokenType.IDENTIFIER, TokenType.SEMICOLON, TokenType.R_BRACE)
        val values = mutableListOf<String>()
        if (!ctx.match(TokenType.SEMICOLON) && !ctx.nextIs(TokenType.R_BRACE)) {
            do {
                values += ctx.eat(TokenType.IDENTIFIER).value
                ctx.skipOnlyUntil(TokenType.L_PAREN)
                if (ctx.match(TokenType.L_PAREN)) {
                    //TODO Constructor parameters for Enum values
                    ctx.skipOnlyUntil(TokenType.R_PAREN)
                    ctx.eat(TokenType.R_PAREN)
                }
                ctx.skipOnlyUntil(TokenType.L_BRACE)
                if (ctx.match(TokenType.L_BRACE)) {
                    //TODO Object body for Enum values
                    ctx.skipOnlyUntil(TokenType.R_BRACE)
                    ctx.eat(TokenType.R_BRACE)
                }

                ctx.skipOnlyUntil(TokenType.SEMICOLON, TokenType.R_BRACE, TokenType.COMMA)
            } while (ctx.match(TokenType.COMMA))
        }

        val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
        if (node !is MultiNode) return invalidNode(node)
        val (decl, other) = node.list.remapInitializers().partition { it is Declaration }
        if (other.isNotEmpty()) return invalidChildNodes(node.list)
        return DeclareEnumClassNode(ident.value, values, decl.filterIsInstance<Declaration>(), ident.section)
    }
}