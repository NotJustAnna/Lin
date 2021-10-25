package com.github.adriantodt.lin.ast.node.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

class BinaryOperation(
    val left: Expr, val right: Expr, val operator: BinaryOperationType, override val section: Section
) : Expr {
    /* @automation(ast.impl BinaryOperation)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitBinaryOperation(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitBinaryOperation(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitBinaryOperation(this, param0)
    /* @automation-end */
}
