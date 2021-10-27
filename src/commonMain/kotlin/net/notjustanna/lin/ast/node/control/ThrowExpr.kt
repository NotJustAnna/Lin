package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class ThrowExpr(val value: Expr, override val section: Section) : Expr {
    /* @automation(ast.impl ThrowExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitThrowExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitThrowExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitThrowExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitThrowExpr(this, param0)
    /* @automation-end */
}
