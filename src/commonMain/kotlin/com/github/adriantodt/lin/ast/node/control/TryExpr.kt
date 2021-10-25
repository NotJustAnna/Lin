package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

/*
 * NOTE: TryExpr is one of those dynamic expressions which may explicitly return `unit`
 * and be an expression, even though the branch had an regular node.
 */

data class TryExpr(
    val tryBranch: Node,
    val catchBranch: CatchBranch?,
    val finallyBranch: Node?,
    override val section: Section
) : Expr {
    /* @automation(ast.impl TryExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitTryExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitTryExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitTryExpr(this, param0)
    /* @automation-end */
}


