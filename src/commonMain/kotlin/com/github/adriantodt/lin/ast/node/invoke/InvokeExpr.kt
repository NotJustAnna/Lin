package com.github.adriantodt.lin.ast.node.invoke

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class InvokeExpr(val target: Expr, val arguments: List<Expr>, override val section: Section) : Expr {
    /* @automation(ast.impl InvokeExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitInvokeExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitInvokeExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeExpr(this, param0)
    /* @automation-end */
}
