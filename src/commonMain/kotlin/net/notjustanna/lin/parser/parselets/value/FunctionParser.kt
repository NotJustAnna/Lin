package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.Expr
import net.notjustanna.lin.ast.FunctionExpr
import net.notjustanna.lin.ast.InvalidNode
import net.notjustanna.lin.ast.Node
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.lin.parser.utils.parseBlock
import net.notjustanna.lin.parser.utils.skipOnlyUntil
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object FunctionParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
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
        ctx.maybeIgnoreNL()

        return FunctionExpr(parameters, ident?.value, expr, token.section)
    }
}
