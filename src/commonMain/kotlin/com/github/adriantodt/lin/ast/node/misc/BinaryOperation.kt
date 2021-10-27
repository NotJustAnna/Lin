package com.github.adriantodt.lin.ast.node.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class BinaryOperation(
    val left: Expr, val right: Expr, val operator: BinaryOperationType, override val section: Section
) : Expr {
    /* @automation(ast.impl BinaryOperation,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitBinaryOperation(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitBinaryOperation(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitBinaryOperation(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitBinaryOperation(this, param0)
    /* @automation-end */
}
