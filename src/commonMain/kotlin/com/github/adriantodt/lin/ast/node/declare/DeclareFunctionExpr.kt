package com.github.adriantodt.lin.ast.node.declare

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.value.FunctionExpr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class DeclareFunctionExpr(val name: String, val value: FunctionExpr, override val section: Section? = null) :
    Expr {
    /* @automation(ast.impl DeclareFunctionExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitDeclareFunctionExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitDeclareFunctionExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitDeclareFunctionExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDeclareFunctionExpr(this, param0)
    /* @automation-end */
}
