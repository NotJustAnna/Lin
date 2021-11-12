package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class ContinueExpr(override val section: Section? = null) : Expr {
    /* @automation(ast.impl ContinueExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitContinueExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitContinueExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitContinueExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitContinueExpr(this, param0)
    /* @automation-end */
}

