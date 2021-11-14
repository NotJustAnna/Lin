package com.github.adriantodt.lin.parser.parselets.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.access.IdentifierExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.value.*
import com.github.adriantodt.lin.lexer.DoubleToken
import com.github.adriantodt.lin.lexer.LongToken
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.lexer.TokenType.*
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.lin.parser.utils.maybeIgnoreNL
import com.github.adriantodt.lin.parser.utils.skipOnlyUntil
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.StringToken
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

object ObjectParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        val contents = mutableListOf<Pair<Expr, Expr>>()

        ctx.matchAll(NL)
        if (!ctx.match(R_BRACE)) {
            do {
                ctx.matchAll(NL)

                val key: Expr
                if (ctx.nextIsAny(STRING, IDENTIFIER)) {
                    val keyToken = ctx.eat() as StringToken
                    key = StringExpr(keyToken.value, keyToken.section)
                    // TODO Implement `operator` modifier for functions (Yes it is implemented here)
                    if (keyToken.type == IDENTIFIER && ctx.skipOnlyUntil(COMMA)) {
                        contents += key to IdentifierExpr(keyToken.value, keyToken.section)
                        continue
                    }
                } else if (ctx.nextIs(FUN)) {
                    val func = ctx.parseExpression()

                    contents += when {
                        func is DeclareFunctionExpr -> StringExpr(func.name, func.section) to func.value
                        func is FunctionExpr && func.name != null -> StringExpr(func.name, func.section) to func
                        else -> return InvalidNode {
                            section(token.section)
                            child(func)
                            error(SyntaxException("Expected a named function", func.section))
                        }
                    }
                    continue
                } else if (ctx.nextIsAny(INTEGER, DECIMAL, TRUE, FALSE)) {
                    val keyToken = ctx.eat()
                    key = when (keyToken.type) {
                        INTEGER -> IntegerExpr((keyToken as LongToken).value, keyToken.section)
                        DECIMAL -> DecimalExpr((keyToken as DoubleToken).value, keyToken.section)
                        TRUE -> BooleanExpr(true, keyToken.section)
                        FALSE -> BooleanExpr(false, keyToken.section)
                        else -> throw AssertionError("[INTERNAL] Impossible token type")
                    }
                } else if (ctx.match(L_BRACKET)) {
                    key = ctx.parseExpression().let {
                        it as? Expr ?: InvalidNode {
                            section(token.section)
                            child(it)
                            error(SyntaxException("Expected an expression", it.section))
                        }
                    }
                    ctx.eat(R_BRACKET)
                } else {
                    key = InvalidNode {
                        if (ctx.eof) {
                            section(token.section)
                            error(SyntaxException("Expected an expression, got EOF", token.section))
                        } else {
                            val actual = ctx.eat()
                            section(actual.section)
                            error(SyntaxException("${actual.type} is not a valid object key", actual.section))
                        }
                    }
                }

                ctx.eat(COLON)

                contents += key to ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }


                ctx.matchAll(NL)
            } while (ctx.match(COMMA))
            ctx.matchAll(NL)
            ctx.eat(R_BRACE)
            ctx.maybeIgnoreNL()
        }
        return ObjectExpr(contents, token.section)
    }
}
