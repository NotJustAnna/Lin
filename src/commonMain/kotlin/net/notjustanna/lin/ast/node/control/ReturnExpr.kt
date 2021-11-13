package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class ReturnExpr(val value: Expr, override val section: Section? = null) : Expr {
    /* @automation(ast.impl ReturnExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitReturnExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitReturnExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitReturnExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitReturnExpr(this, param0)
    /* @automation-end */
}
