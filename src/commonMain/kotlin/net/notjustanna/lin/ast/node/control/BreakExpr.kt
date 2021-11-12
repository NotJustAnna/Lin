package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class BreakExpr(override val section: Section? = null) : Expr {
    /* @automation(ast.impl BreakExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitBreakExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitBreakExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitBreakExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitBreakExpr(this, param0)
    /* @automation-end */
}
