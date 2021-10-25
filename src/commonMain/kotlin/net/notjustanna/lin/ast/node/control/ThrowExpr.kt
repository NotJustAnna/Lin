package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class ThrowExpr(val value: Expr, override val section: Section) : Expr {
    /* @automation(ast.impl ThrowExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitThrowExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitThrowExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitThrowExpr(this, param0)
    /* @automation-end */
}
