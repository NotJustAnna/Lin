package io.github.cafeteriaguild.lin.ast.node

import net.notjustanna.tartar.api.lexer.Sectional

interface Node : Sectional {
    fun <R> accept(visitor: NodeVisitor<R>): R
    fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T): R
}