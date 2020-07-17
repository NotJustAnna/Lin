package io.github.cafeteriaguild.lin.ast.expr

import net.notjustanna.tartar.api.lexer.Sectional

interface Expr : Sectional {
    fun <R> accept(visitor: ExprVisitor<R>): R
    fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T): R
}