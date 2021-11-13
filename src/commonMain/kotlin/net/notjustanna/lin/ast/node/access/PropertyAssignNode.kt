package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class PropertyAssignNode(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    val value: Expr,
    override val section: Section? = null
) : Node {
    /* @automation(ast.impl PropertyAssignNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitPropertyAssignNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitPropertyAssignNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitPropertyAssignNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitPropertyAssignNode(this, param0)
    /* @automation-end */
}
