package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class FunctionExpr(
    val parameters: List<Parameter>,
    val name: String?,
    val body: Node?,
    override val section: Section? = null
): Expr {
    /* @automation(ast.impl FunctionExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitFunctionExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitFunctionExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitFunctionExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitFunctionExpr(this, param0)
    /* @automation-end */

    data class Parameter(val name: String, val varargs: Boolean, val defaultValue: Expr?)
}
