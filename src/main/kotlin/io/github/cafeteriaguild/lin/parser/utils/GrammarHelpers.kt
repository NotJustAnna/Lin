package io.github.cafeteriaguild.lin.parser.utils

import net.notjustanna.tartar.api.parser.ParserContext
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

/**
 * Hacky method to ignore newlines in case of method chains
 */
fun ParserContext<TokenType, Expr>.maybeIgnoreNL() {
    if (peekAheadUntil(TokenType.DOT).all { it.type == TokenType.NL }) {
        skipUntil(TokenType.DOT)
    }
}

fun ParserContext<TokenType, Expr>.parseBlock(): Expr {
    if (match(TokenType.L_BRACE)) {
        val start = last
        val list = mutableListOf<Expr>()
        while (true) {
            matchAll(TokenType.NL, TokenType.SEMICOLON)
            if (match(TokenType.R_BRACE)) break
            list += parseExpression()
        }
        return MultiExpr(list, start.section)
    }
    return parseExpression()
}