package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.ConstExpr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class FloatExpr(val value: Float, override val section: Section) : ConstExpr {
    /* @automation(ast.impl FloatExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitFloatExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitFloatExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitFloatExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitFloatExpr(this, param0)
    /* @automation-end */
}
