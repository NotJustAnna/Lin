package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class AssignNode(val name: String, val value: Expr, override val section: Section) : Node {
    /* @automation(ast.impl AssignNode)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitAssignNode(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitAssignNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitAssignNode(this, param0)
    /* @automation-end */
}
