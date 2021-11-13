package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class ObjectExpr(val value: List<Pair<Expr, Expr>>, override val section: Section? = null) : Expr {
    /* @automation(ast.impl ObjectExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitObjectExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitObjectExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitObjectExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitObjectExpr(this, param0)
    /* @automation-end */
}
