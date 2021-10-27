package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

data class NullExpr(override val section: Section) : ConstExpr {
    /* @automation(ast.impl NullExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitNullExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitNullExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitNullExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitNullExpr(this, param0)
    /* @automation-end */
}
