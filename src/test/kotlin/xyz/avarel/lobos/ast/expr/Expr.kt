package xyz.avarel.lobos.ast.expr

import xyz.avarel.lobos.lexer.Sectional

interface Expr : Sectional {
    fun <R> accept(visitor: ExprVisitor<R>): R
}