package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class DoubleExpr(val value: Double, override val section: Section) : Expr {
    /* @automation(ast.impl DoubleExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitDoubleExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitDoubleExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoubleExpr(this, param0)
    /* @automation-end */
}
