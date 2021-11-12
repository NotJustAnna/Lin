package com.github.adriantodt.lin.ast.node.access

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class SubscriptAccessExpr(
    val target: Expr,
    val arguments: List<Expr>,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl SubscriptAccessExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitSubscriptAccessExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitSubscriptAccessExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitSubscriptAccessExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitSubscriptAccessExpr(this, param0)
    /* @automation-end */
}
