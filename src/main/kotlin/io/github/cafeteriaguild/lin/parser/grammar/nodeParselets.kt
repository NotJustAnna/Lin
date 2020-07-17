package io.github.cafeteriaguild.lin.parser.grammar

import net.notjustanna.tartar.api.GrammarDSL
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.AssignExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*

fun GrammarDSL<TokenType, Expr>.nodeParselets() {
    numberParselets()
    prefix(NULL) { NullExpr(it.section) }
    prefix(TRUE) { BooleanExpr(true, it.section) }
    prefix(FALSE) { BooleanExpr(false, it.section) }
    prefix(IDENTIFIER) {
        if (match(ASSIGN)) {
            AssignExpr(
                it.value,
                parseExpression(),
                it.section
            )
        } else {
            IdentExpr(it.value, it.section)
        }
    }
    prefix(STRING) { StringExpr(it.value, it.section) }
    prefix(RETURN) {
        val expr = if (!eof && !matchAny(NL, SEMICOLON)) {
            parseExpression()
        } else UnitExpr(it.section)

        ReturnExpr(expr, it.span(expr))
    }
}

