package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class DoubleExpr(val value: Double, override val section: Section) : Expr {
    /* @automation(ast.impl DoubleExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitDoubleExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitDoubleExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoubleExpr(this, param0)
    /* @automation-end */
}
