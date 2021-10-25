package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class ReturnExpr(val value: Expr, override val section: Section) : Expr {
    /* @automation(ast.impl ReturnExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitReturnExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitReturnExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitReturnExpr(this, param0)
    /* @automation-end */
}
