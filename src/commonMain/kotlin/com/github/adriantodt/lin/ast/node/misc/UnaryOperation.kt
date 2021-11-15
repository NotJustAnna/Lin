package com.github.adriantodt.lin.ast.node.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.lin.utils.UnaryOperationType
import com.github.adriantodt.tartar.api.lexer.Section

public data class UnaryOperation(
    val target: Expr,
    val operator: UnaryOperationType,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl UnaryOperation,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitUnaryOperation(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitUnaryOperation(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitUnaryOperation(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitUnaryOperation(this, param0)
    }
    /* @automation-end */
}
