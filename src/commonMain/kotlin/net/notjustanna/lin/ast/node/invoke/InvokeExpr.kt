package net.notjustanna.lin.ast.node.invoke

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class InvokeExpr(
    val target: Expr,
    val arguments: List<Expr>,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl InvokeExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitInvokeExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitInvokeExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitInvokeExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitInvokeExpr(this, param0)
    }
    /* @automation-end */
}
