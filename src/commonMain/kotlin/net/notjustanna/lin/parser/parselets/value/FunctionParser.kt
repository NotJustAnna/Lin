package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.value.FunctionExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.matchAll
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.lin.parser.utils.parseBlock
import net.notjustanna.lin.parser.utils.skipOnlyUntil
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.StringToken
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object FunctionParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) as StringToken else null
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)

        val parameters = mutableListOf<FunctionExpr.Parameter>()

        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                ctx.matchAll(TokenType.NL)
                val isVarargs: Boolean
                val paramName = (ctx.eat(TokenType.IDENTIFIER) as StringToken).let {
                    if (it.value == "vararg" && ctx.nextIs(TokenType.IDENTIFIER)) {
                        isVarargs = true
                        (ctx.eat(TokenType.IDENTIFIER) as StringToken).value
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

        val function = FunctionExpr(parameters, ident?.value, expr, token.section)

        val exceededMaxParameters = parameters.size > 255
        val exceededVarargs = parameters.count { it.varargs } > 1

        if (exceededMaxParameters || exceededVarargs) {
            return InvalidNode {
                if (exceededMaxParameters) {
                    error(SyntaxException("Function exceeded maximum of 255 parameters", token.section))
                }
                if (exceededVarargs) {
                    error(SyntaxException("Function has more than 1 varargs parameter", token.section))
                }
                child(function)
            }
        }

        return function
    }
}
