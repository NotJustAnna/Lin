package net.notjustanna.lin.ast.node.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.lin.utils.BinaryOperationType
import net.notjustanna.tartar.api.lexer.Section

data class BinaryOperation(
    val left: Expr, val right: Expr, val operator: BinaryOperationType, override val section: Section? = null
) : Expr {
    /* @automation(ast.impl BinaryOperation,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitBinaryOperation(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitBinaryOperation(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitBinaryOperation(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitBinaryOperation(this, param0)
    /* @automation-end */
}
