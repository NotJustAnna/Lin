package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class IfExpr(
    val condition: Expr,
    val thenBranch: Expr,
    val elseBranch: Expr,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl IfExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitIfExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitIfExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitIfExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitIfExpr(this, param0)
    }
    /* @automation-end */
}
