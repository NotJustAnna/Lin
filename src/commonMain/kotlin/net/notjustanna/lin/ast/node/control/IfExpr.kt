package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class IfExpr(
    val condition: Expr,
    val thenBranch: Expr,
    val elseBranch: Expr,
    override val section: Section
) : Expr {
    /* @automation(ast.impl IfExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitIfExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitIfExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitIfExpr(this, param0)
    /* @automation-end */
}
