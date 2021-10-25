package net.notjustanna.lin.ast.node.access

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class PropertyAccessExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    override val section: Section
) : Expr {
    /* @automation(ast.impl PropertyAccessExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitPropertyAccessExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitPropertyAccessExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitPropertyAccessExpr(this, param0)
    /* @automation-end */
}
