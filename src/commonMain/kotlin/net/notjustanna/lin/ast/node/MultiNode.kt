package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

/**
 * This represents a given list of nodes.
 * Nodes must be executed sequentially.
 * This AST node's main use is function bodies and main scopes.
 */
data class MultiNode(val list: List<Node>, override val section: Section) : Node {
    /* @automation(ast.impl MultiNode)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitMultiNode(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitMultiNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitMultiNode(this, param0)
    /* @automation-end */
}
