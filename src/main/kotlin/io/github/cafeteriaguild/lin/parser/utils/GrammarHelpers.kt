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
fun ParserContext<TokenType, Expr>.maybeIgnoreNL() {
    skipNewLinesUntil(TokenType.DOT, TokenType.QUESTION_DOT)
}

fun ParserContext<TokenType, Expr>.skipNewLinesUntil(vararg type: TokenType) {
    check(TokenType.NL !in type) { "[INTERNAL] NL was supplied as a token" }
    if (peekAheadUntil(*type).all { it.type == TokenType.NL }) {
        skipUntil(*type)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun ParserContext<TokenType, Expr>.parseBlock(smartToNode: Boolean = true): Expr? {
    if (match(TokenType.L_BRACE)) {
        val start = last
        val list = mutableListOf<Expr>()
        while (true) {
            matchAll(TokenType.NL, TokenType.SEMICOLON)
            if (match(TokenType.R_BRACE)) break
            list += parseExpression()
        }
        if (smartToNode && list.isNotEmpty() && list.last() is Node) {
            val last = list.removeLast() as Node
            return MultiNode(list, last, start.section)
        }
        return MultiExpr(list, start.section)
    }
    return null
}