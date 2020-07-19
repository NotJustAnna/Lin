package io.github.cafeteriaguild.lin.parser.utils

import net.notjustanna.tartar.api.parser.ParserContext
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType

/**
 * Hacky method to ignore newlines in case of method chains
 */
fun ParserContext<TokenType, Node>.maybeIgnoreNL() {
    skipOnlyUntil(TokenType.DOT, TokenType.QUESTION_DOT)
}

fun ParserContext<TokenType, Node>.skipOnlyUntil(vararg type: TokenType) {
    check(TokenType.NL !in type) { "[INTERNAL] NL was supplied as a token" }
    if (peekAheadUntil(*type).all { it.type == TokenType.NL }) {
        skipUntil(*type)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun ParserContext<TokenType, Node>.parseBlock(smartToExpr: Boolean = true, braceConsumed: Boolean = false): Node? {
    if (braceConsumed || match(TokenType.L_BRACE)) {
        val start = last
        val list = mutableListOf<Node>()
        while (true) {
            matchAll(TokenType.SEMICOLON)
            skipOnlyUntil(TokenType.R_BRACE)
            if (match(TokenType.R_BRACE)) break
            matchAll(TokenType.NL)
            list += parseExpression()
        }
        if (smartToExpr && list.isNotEmpty() && list.last() is Expr) {
            val last = list.removeLast() as Expr
            return MultiExpr(list, last, start.section)
        }
        return MultiNode(list, start.section)
    }
    return null
}