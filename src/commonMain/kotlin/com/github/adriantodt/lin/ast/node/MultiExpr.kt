package com.github.adriantodt.lin.ast.node

import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

/**
 * This represents a given list of nodes, with the last node being necessarily an expression.
 * Nodes must be executed sequentially. The value of this expression is the last expression's value.
 * This AST node's main use is lambda bodies and REPL scopes.
 */
data class MultiExpr(val list: List<Node>, val last: Expr, override val section: Section) : Expr, Node.Multi {
    /* @automation(ast.impl MultiExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitMultiExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitMultiExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitMultiExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitMultiExpr(this, param0)
    /* @automation-end */

    override fun nodes() = list + last

    override fun lastNode() = last
}
