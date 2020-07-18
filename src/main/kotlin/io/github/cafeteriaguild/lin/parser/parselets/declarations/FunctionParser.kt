package io.github.cafeteriaguild.lin.parser.parselets.declarations

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.DeclareFunctionExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.FunctionNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

object FunctionParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        ctx.matchAll(TokenType.NL)
        val ident = if (ctx.nextIs(TokenType.IDENTIFIER)) ctx.eat(TokenType.IDENTIFIER) else null
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)

        val parameters = mutableListOf<FunctionNode.Parameter>()

        if (!ctx.match(TokenType.R_PAREN)) {
            do {
                ctx.matchAll(TokenType.NL)
                val paramIdent = ctx.eat(TokenType.IDENTIFIER)
                ctx.matchAll(TokenType.NL)
                val paramValue = if (ctx.match(TokenType.ASSIGN)) {
                    ctx.matchAll(TokenType.NL)
                    ctx.parseExpression().let {
                        it as? Node ?: return InvalidExpr {
                            section(token.section)
                            child(it)
                            error(SyntaxException("Expected a node", it.section))
                        }
                    }
                } else null
                ctx.matchAll(TokenType.NL)

                parameters += FunctionNode.Parameter(paramIdent.value, paramValue)
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_PAREN)
        }
        ctx.matchAll(TokenType.NL)
        val expr = if (ctx.match(TokenType.ASSIGN)) {
            ctx.matchAll(TokenType.NL)
            ctx.parseExpression().let {
                it as? Node ?: return InvalidExpr {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a node", it.section))
                }
            }
        } else {
            ctx.parseBlock(false) ?: return InvalidExpr {
                section(token.section)
                error(SyntaxException("Couldn't parse function's block, found ${ctx.peek()}", token.section))
            }
        }

        val function = FunctionNode(parameters, expr, token.section)

        return if (ident != null) DeclareFunctionExpr(ident.value, function, ident.section) else function
    }
}