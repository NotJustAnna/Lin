package com.github.adriantodt.lin.ast.node.access

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class IdentifierExpr(val name: String, override val section: Section) : Expr {
    /* @automation(ast.impl IdentifierExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitIdentifierExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitIdentifierExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitIdentifierExpr(this, param0)
    /* @automation-end */
}
