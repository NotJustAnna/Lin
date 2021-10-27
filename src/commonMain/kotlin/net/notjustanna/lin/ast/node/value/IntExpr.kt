package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class IntExpr(val value: Int, override val section: Section) : ConstExpr {
    /* @automation(ast.impl IntExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitIntExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitIntExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitIntExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitIntExpr(this, param0)
    /* @automation-end */
}
