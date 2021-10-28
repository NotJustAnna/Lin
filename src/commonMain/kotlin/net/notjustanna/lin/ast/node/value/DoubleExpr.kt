package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class DoubleExpr(val value: Double, override val section: Section) : ConstExpr {
    /* @automation(ast.impl DoubleExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitDoubleExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitDoubleExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitDoubleExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoubleExpr(this, param0)
    /* @automation-end */
}
