package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class UnitExpr(override val section: Section) : ConstExpr {
    /* @automation(ast.impl UnitExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitUnitExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitUnitExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitUnitExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitUnitExpr(this, param0)
    /* @automation-end */
}
