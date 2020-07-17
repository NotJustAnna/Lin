package io.github.cafeteriaguild.lin.parser.grammar

import net.notjustanna.tartar.api.GrammarDSL
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.nodes.DoubleExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.FloatExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IntExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.LongExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*

fun GrammarDSL<TokenType, Expr>.numberParselets() {
    prefix(INT) { IntExpr(it.value.toInt(), it.section) }
    prefix(LONG) { LongExpr(it.value.toLong(), it.section) }
    prefix(FLOAT) {
        FloatExpr(
            it.value.toFloat(),
            it.section
        )
    }
    prefix(DOUBLE) {
        DoubleExpr(
            it.value.toDouble(),
            it.section
        )
    }
}

