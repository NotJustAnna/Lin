package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Sectional

/**
 * This is a simple AST node from Lin.
 * Nodes that don't extend [Expr] are meant to have no value.
 * If you have to extract a value from a Node which doesn't extend [Expr], consider it's value `unit`.
 */
interface Node : Sectional {
    /* @automation(ast.node)-start */
    fun accept(visitor: NodeVisitor0)

    fun <R> accept(visitor: NodeVisitor0R<R>): R

    fun <T> accept(visitor: NodeVisitor1<T>, param0: T)
    /* @automation-end */
}
