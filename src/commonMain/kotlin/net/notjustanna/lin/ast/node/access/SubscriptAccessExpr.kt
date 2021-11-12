package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class SubscriptAccessExpr(
    val target: Expr,
    val arguments: List<Expr>,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl SubscriptAccessExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitSubscriptAccessExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitSubscriptAccessExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitSubscriptAccessExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitSubscriptAccessExpr(this, param0)
    /* @automation-end */
}
