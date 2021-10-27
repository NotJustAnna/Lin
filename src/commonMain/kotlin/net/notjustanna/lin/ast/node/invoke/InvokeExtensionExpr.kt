package net.notjustanna.lin.ast.node.invoke

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class InvokeExtensionExpr(
    val left: Expr, val nullSafe: Boolean, val right: Expr, val arguments: List<Expr>, override val section: Section
) : Expr {
    /* @automation(ast.impl InvokeExtensionExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitInvokeExtensionExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitInvokeExtensionExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitInvokeExtensionExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeExtensionExpr(this, param0)
    /* @automation-end */
}

