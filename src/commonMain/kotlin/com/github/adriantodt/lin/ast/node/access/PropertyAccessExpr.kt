package com.github.adriantodt.lin.ast.node.access

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class PropertyAccessExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    override val section: Section
) : Expr {
    /* @automation(ast.impl PropertyAccessExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitPropertyAccessExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitPropertyAccessExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitPropertyAccessExpr(this, param0)
    /* @automation-end */
}
