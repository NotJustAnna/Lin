package net.notjustanna.lin.grammar.parselets.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.access.IdentifierExpr
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.value.*
import net.notjustanna.lin.grammar.utils.skipOnlyUntil
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.lexer.TokenType.*
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.lin.parser.utils.matchAll

object ObjectParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val contents = mutableListOf<Pair<Expr, Expr>>()

        ctx.matchAll(NL)
        if (!ctx.match(R_BRACE)) {
            do {
                ctx.matchAll(NL)

                val key: Expr
                if (ctx.nextIsAny(STRING, IDENTIFIER)) {
                    val (type, value, section) = ctx.eat()
                    key = StringExpr(value, section)
                    // TODO Implement `operator` modifier for functions (Yes it is implemented here)
                    if (type == IDENTIFIER && ctx.skipOnlyUntil(COMMA)) {
                        contents += key to IdentifierExpr(value, section)
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
                    val (type, value, section) = ctx.eat()
                    key = when (type) {
                        INTEGER -> IntegerExpr(value.toLong(), section)
                        DECIMAL -> DecimalExpr(value.toDouble(), section)
                        TRUE -> BooleanExpr(true, section)
                        FALSE -> BooleanExpr(false, section)
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
            ctx.eat(R_BRACE)
        }
        return ObjectExpr(contents, token.section)
    }
}
