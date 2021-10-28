package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.ConstExpr
import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class UnitExpr(override val section: Section) : ConstExpr {
    /* @automation(ast.impl UnitExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitUnitExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitUnitExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitUnitExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitUnitExpr(this, param0)
    /* @automation-end */
}
