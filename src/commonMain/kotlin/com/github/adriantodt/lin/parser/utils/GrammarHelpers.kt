package com.github.adriantodt.lin.parser.utils

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.value.FunctionExpr
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext

/**
 * This method consumes [TokenType.NL] tokens to allow some multiline neatness.
 *
 * This method usually is called at the end of expressions to allow some neat chaining.
 */
fun ParserContext<TokenType, Node>.maybeIgnoreNL() {
    skipOnlyUntil(
        TokenType.DOT, TokenType.QUESTION_DOT, TokenType.AND, TokenType.OR
    )
}

fun ParserContext<TokenType, Node>.skipOnlyUntil(vararg type: TokenType): Boolean {
    check(TokenType.NL !in type) { "[INTERNAL] NL was supplied as a token" }
    if (peekAheadUntil(*type).all { it.type == TokenType.NL }) {
        skipUntil(*type)
        return true
    }
    return false
}

fun ParserContext<TokenType, Node>.parseBlock(smartToExpr: Boolean = true, braceConsumed: Boolean = false): Node? {
    if (braceConsumed || match(TokenType.L_BRACE)) {
        val start = last
        val list = mutableListOf<Node>()
        while (true) {
            matchAll(TokenType.SEMICOLON)
            skipOnlyUntil(TokenType.R_BRACE)
            if (match(TokenType.R_BRACE)) break
            matchAll(TokenType.NL)
            val expr = parseExpression()

            list += when {
                expr is FunctionExpr && expr.name != null -> DeclareFunctionExpr(expr.name, expr, expr.section)
                else -> expr
            }
        }
        if (smartToExpr && list.isNotEmpty() && list.last() is Expr) {
            val last = list.removeLast() as Expr
            return MultiExpr(list, last, start.section)
        }
        return MultiNode(list, start.section)
    }
    return null
}
