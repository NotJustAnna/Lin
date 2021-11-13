package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class ThrowExpr(val value: Expr, override val section: Section? = null) : Expr {
    /* @automation(ast.impl ThrowExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitThrowExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitThrowExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitThrowExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitThrowExpr(this, param0)
    /* @automation-end */
}
