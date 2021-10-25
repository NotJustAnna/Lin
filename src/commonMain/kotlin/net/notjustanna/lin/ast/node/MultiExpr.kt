package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

/**
 * This represents a given list of nodes, with the last node being necessarily an expression.
 * Nodes must be executed sequentially. The value of this expression is the last expression's value.
 * This AST node's main use is lambda bodies and REPL scopes.
 */
data class MultiExpr(val list: List<Node>, val last: Expr, override val section: Section) : Expr {
    /* @automation(ast.impl MultiExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitMultiExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitMultiExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitMultiExpr(this, param0)
    /* @automation-end */
}
