package com.github.adriantodt.lin.ast.node

import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Sectional

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
