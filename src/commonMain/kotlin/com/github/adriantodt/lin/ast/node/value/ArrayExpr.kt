package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class ArrayExpr(val value: List<Expr>, override val section: Section) : Expr {
    /* @automation(ast.impl ArrayExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitArrayExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitArrayExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitArrayExpr(this, param0)
    /* @automation-end */
}
