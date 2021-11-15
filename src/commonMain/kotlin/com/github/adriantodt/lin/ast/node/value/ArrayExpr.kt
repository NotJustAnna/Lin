package com.github.adriantodt.lin.ast.node.value

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class ArrayExpr(val value: List<Expr>, override val section: Section? = null) : Expr {
    /* @automation(ast.impl ArrayExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitArrayExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitArrayExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitArrayExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitArrayExpr(this, param0)
    }
    /* @automation-end */
}
