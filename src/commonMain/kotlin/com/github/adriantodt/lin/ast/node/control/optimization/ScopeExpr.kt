package com.github.adriantodt.lin.ast.node.control.optimization

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class ScopeExpr(val body: Expr, override val section: Section) : Expr {
    /* @automation(ast.impl ScopeExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitScopeExpr(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitScopeExpr(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitScopeExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitScopeExpr(this, param0)
    /* @automation-end */
}
