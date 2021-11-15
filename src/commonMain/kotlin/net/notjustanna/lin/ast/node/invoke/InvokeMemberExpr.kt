package net.notjustanna.lin.ast.node.invoke

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class InvokeMemberExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    val arguments: List<Expr>,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl InvokeMemberExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitInvokeMemberExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitInvokeMemberExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitInvokeMemberExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitInvokeMemberExpr(this, param0)
    }
    /* @automation-end */
}
