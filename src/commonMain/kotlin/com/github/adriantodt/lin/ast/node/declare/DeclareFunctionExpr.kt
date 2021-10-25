package com.github.adriantodt.lin.ast.node.declare

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.value.FunctionExpr
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

class DeclareFunctionExpr(val name: String, val value: FunctionExpr, override val section: Section): Expr {
    /* @automation(ast.impl DeclareFunctionExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitDeclareFunctionExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitDeclareFunctionExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDeclareFunctionExpr(this, param0)
    /* @automation-end */
}
