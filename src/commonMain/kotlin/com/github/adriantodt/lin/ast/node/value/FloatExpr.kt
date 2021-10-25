package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class FloatExpr(val value: Float, override val section: Section) : Expr {
    /* @automation(ast.impl FloatExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitFloatExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitFloatExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitFloatExpr(this, param0)
    /* @automation-end */
}
