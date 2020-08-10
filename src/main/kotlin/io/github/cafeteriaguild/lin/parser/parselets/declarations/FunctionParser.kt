package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareFunctionNode
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.nodes.FunctionExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.parselets.nodes.IdentifierParser
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL
import io.github.cafeteriaguild.lin.parser.utils.parseBlock
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object FunctionParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        if (ctx.nextIs(TokenType.INTERFACE)) {
            return IdentifierParser.applyModifiers(ctx.parseExpression(), mapOf(LinModifier.FUN to token))
        }
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) else null
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)

        val parameters = mutableListOf<FunctionExpr.Parameter>()

        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                ctx.matchAll(TokenType.NL)
                val isVarargs: Boolean
                val paramName = ctx.eat(TokenType.IDENTIFIER).let {
                    if (it.value == "vararg" && ctx.nextIs(TokenType.IDENTIFIER)) {
                        isVarargs = true
                        ctx.eat(TokenType.IDENTIFIER).value
                    } else {
                        isVarargs = false
                        it.value
                    }
                }
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

                parameters += FunctionExpr.Parameter(paramName, isVarargs, paramValue)
            } while (ctx.match(TokenType.COMMA))
            ctx.matchAll(TokenType.NL)
            ctx.eat(TokenType.R_PAREN)
        }
        ctx.skipOnlyUntil(TokenType.ASSIGN, TokenType.L_BRACE)
        val expr = when {
            ctx.match(TokenType.ASSIGN) -> {
                ctx.matchAll(TokenType.NL)
                ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }
            }
            ctx.match(TokenType.L_BRACE) -> {
                ctx.parseBlock(smartToExpr = false, braceConsumed = true)
            }
            else -> null
        }
        if (ident == null) {
            ctx.maybeIgnoreNL()
        }

        val function = FunctionExpr(parameters, expr, token.section)

        return if (ident != null) DeclareFunctionNode(ident.value, function, ident.section) else function
    }
}