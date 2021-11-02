package com.github.adriantodt.lin.ast.node.invoke

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class InvokeExpr(val target: Expr, val arguments: List<Expr>, override val section: Section) : Expr {
    /* @automation(ast.impl InvokeExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitInvokeExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitInvokeExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitInvokeExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeExpr(this, param0)
    /* @automation-end */
}