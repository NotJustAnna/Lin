package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.NL
import io.github.cafeteriaguild.lin.lexer.TokenType.SEMICOLON

/** [block] returns false if it wants to skip to the next statement */
inline fun ParserContext<TokenType, Expr>.delimitedBlock(
    delimiterPair: Pair<TokenType, TokenType>? = null,
    block: () -> Boolean
) {
    if (eof) {
        if (index == 0) {
            throw SyntaxException("Expected block but reached end of file", Section(source, 0, 0, 0))
        } else {
            throw SyntaxException("Expected block but reached end of file", last.section)
        }
    }

    delimiterPair?.first?.let(this::eat)
    matchAll(NL, SEMICOLON)

    do {
        if (eof || (delimiterPair != null && nextIs(delimiterPair.second))) {
            break
        }
        if (!block()) { //returns true to skip to the next statement
            if (delimiterPair != null) {
                skipUntil(delimiterPair.second, SEMICOLON, NL)
            } else {
                skipUntil(SEMICOLON, NL)
            }
        }
    } while (!eof && matchAll(NL, SEMICOLON))

    delimiterPair?.second?.let(this::eat)
}

fun ParserContext<TokenType, Expr>.parseStatements(delimiterPair: Pair<TokenType, TokenType>? = null): Expr {
    if (eof) throw SyntaxException("Expected block but reached end of folder", last.section)

    val list = mutableListOf<Expr>()

    delimitedBlock(delimiterPair) {
        val expr = parseExpression(0)

        if (expr is InvalidExpr) {
            false
        } else {
            list += expr
            true
        }
    }

    if (last.type == SEMICOLON) list += UnitExpr(last.section)

    return when {
        list.isEmpty() -> UnitExpr(last.section)
        list.size == 1 -> list[0]
        else -> MultiExpr(list)
    }
}
