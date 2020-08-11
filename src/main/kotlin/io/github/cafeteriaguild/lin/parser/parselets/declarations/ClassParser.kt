package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.AccessExpr
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareClassNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object ClassParser : TypeParser() {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
        val ident = ctx.eat(TokenType.IDENTIFIER)
        val parameters = mutableListOf<DeclareClassNode.Parameter>()
        if (ctx.match(TokenType.L_PAREN)) {
            ctx.skipOnlyUntil(TokenType.R_PAREN)
            if (!ctx.match(TokenType.R_PAREN)) {
                do {
                    ctx.matchAll(TokenType.NL)
                    val isVarargs = if (ctx.nextIsAny(TokenType.IDENTIFIER) && ctx.eat().value == "vararg") {
                        if (!ctx.nextIsAny(TokenType.VAL, TokenType.VAR, TokenType.IDENTIFIER)) {
                            ctx.back()
                            false
                        } else true
                    } else false
                    ctx.matchAll(TokenType.NL)
                    val paramType = when {
                        ctx.match(TokenType.VAL) -> DeclareClassNode.ParameterType.VAL
                        ctx.match(TokenType.VAR) -> DeclareClassNode.ParameterType.VAR
                        else -> DeclareClassNode.ParameterType.ARGUMENT
                    }
                    ctx.matchAll(TokenType.NL)
                    val paramName = ctx.eat(TokenType.IDENTIFIER).value
                    ctx.matchAll(TokenType.NL)
                    val paramValue = if (ctx.match(TokenType.ASSIGN)) {
                        ctx.matchAll(TokenType.NL)
                        ctx.parseExpression().let {
                            it as? Expr ?: return InvalidNode {
                                section(token.section)
                                child(it)
                                error(SyntaxException("Expected an expression", it.section))
                            }
                        }
                    } else null
                    ctx.matchAll(TokenType.NL)

                    parameters += DeclareClassNode.Parameter(paramName, isVarargs, paramType, paramValue)
                } while (ctx.match(TokenType.COMMA))
                ctx.skipOnlyUntil(TokenType.R_PAREN)
                ctx.eat(TokenType.R_PAREN)
            }
        }
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
            return DeclareClassNode(ident.value, parameters, implements, emptyList(), ident.section)
        }
        val node = ctx.parseBlockNode() ?: return noBlockParsed(ctx, token)
        if (node !is MultiNode) return invalidNode(node)
        val (decl, other) = node.list.remapInitializers().partition { it is Declaration }
        if (other.isNotEmpty()) return invalidChildNodes(node.list)
        val declarations = decl.filterIsInstance<Declaration>().initializersLast()
        return DeclareClassNode(ident.value, parameters, implements, declarations, ident.section)
    }
}