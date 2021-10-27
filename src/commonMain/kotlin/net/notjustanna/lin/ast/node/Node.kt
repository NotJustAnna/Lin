package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Sectional

/**
 * This is a simple AST node from Lin.
 * Nodes that don't extend [Expr] are meant to have no value.
 * If you have to extract a value from a Node which doesn't extend [Expr], consider it's value `unit`.
 */
interface Node : Sectional {
    /* @automation(ast.root Node)-start */
    fun accept(visitor: NodeVisitor)

    fun accept(visitor: NodeMapVisitor): Node

    fun <R> accept(visitor: NodeVisitorR<R>): R

    fun <T> accept(visitor: NodeVisitor1<T>, param0: T)
    /* @automation-end */

    /**
     * Interfaces implementing this have multiple nodes inside.
     */
    interface Multi : Node {
        fun nodes(): List<Node>

        fun lastNode() : Node
    }
}
