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
    skipNewLinesUntil(TokenType.DOT)
}

fun ParserContext<TokenType, Expr>.skipNewLinesUntil(type: TokenType) {
    check(type != TokenType.NL)
    if (peekAheadUntil(type).all { it.type == TokenType.NL }) {
        skipUntil(type)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun ParserContext<TokenType, Expr>.parseBlock(): Expr {
    skipNewLinesUntil(TokenType.L_BRACE)
    if (match(TokenType.L_BRACE)) {
        val start = last
        val list = mutableListOf<Expr>()
        while (true) {
            matchAll(TokenType.NL, TokenType.SEMICOLON)
            if (match(TokenType.R_BRACE)) break
            list += parseExpression()
        }
        if (list.isNotEmpty() && list.last() is Node) {
            val last = list.removeLast() as Node
            return MultiNode(list, last, start.section)
        }
        return MultiExpr(list, start.section)
    }
    return parseExpression()
}