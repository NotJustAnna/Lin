package com.github.adriantodt.lin.ast.node.invoke

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class InvokeLocalExpr(val name: String, val arguments: List<Expr>, override val section: Section) : Expr {
    /* @automation(ast.impl InvokeLocalExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitInvokeLocalExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitInvokeLocalExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeLocalExpr(this, param0)
    /* @automation-end */
}
