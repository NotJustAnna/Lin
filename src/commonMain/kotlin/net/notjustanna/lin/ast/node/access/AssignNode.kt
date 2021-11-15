package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class AssignNode(val name: String, val value: Expr, override val section: Section? = null) : Node {
    /* @automation(ast.impl AssignNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitAssignNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitAssignNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitAssignNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitAssignNode(this, param0)
    }
    /* @automation-end */
}
