package com.github.adriantodt.lin.ast.node.invoke

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class InvokeMemberExpr(
    val target: Expr, val nullSafe: Boolean, val name: String, val arguments: List<Expr>, override val section: Section
) : Expr {
    /* @automation(ast.impl InvokeMemberExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitInvokeMemberExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitInvokeMemberExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvokeMemberExpr(this, param0)
    /* @automation-end */
}
