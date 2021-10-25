package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class FunctionExpr(
    val parameters: List<Parameter>,
    val name: String?,
    val body: Node?,
    override val section: Section
): Expr {
    /* @automation(ast.impl FunctionExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitFunctionExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitFunctionExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitFunctionExpr(this, param0)
    /* @automation-end */

    data class Parameter(val name: String, val varargs: Boolean, val defaultValue: Expr?)
}
