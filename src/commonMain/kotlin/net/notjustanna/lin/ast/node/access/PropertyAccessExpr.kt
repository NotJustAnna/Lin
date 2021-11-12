package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class PropertyAccessExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl PropertyAccessExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitPropertyAccessExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitPropertyAccessExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitPropertyAccessExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitPropertyAccessExpr(this, param0)
    /* @automation-end */
}
