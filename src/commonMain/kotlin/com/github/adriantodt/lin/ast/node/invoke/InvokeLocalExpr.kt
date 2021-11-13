package com.github.adriantodt.lin.ast.node.invoke

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class InvokeLocalExpr(val name: String, val arguments: List<Expr>, override val section: Section? = null) : Expr {
    /* @automation(ast.impl InvokeLocalExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitInvokeLocalExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitInvokeLocalExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitInvokeLocalExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeLocalExpr(this, param0)
    /* @automation-end */
}
