package net.notjustanna.lin.ast.node.declare

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class DeclareVariableNode(
    val name: String, val mutable: Boolean, val value: Expr?, override val section: Section? = null
) : Node {
    /* @automation(ast.impl DeclareVariableNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitDeclareVariableNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitDeclareVariableNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitDeclareVariableNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitDeclareVariableNode(this, param0)
    }
    /* @automation-end */
}
