package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.ConstExpr
import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class DoubleExpr(val value: Double, override val section: Section) : ConstExpr {
    /* @automation(ast.impl DoubleExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitDoubleExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitDoubleExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitDoubleExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoubleExpr(this, param0)
    /* @automation-end */
}
