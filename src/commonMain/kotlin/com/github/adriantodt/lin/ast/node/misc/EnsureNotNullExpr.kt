package com.github.adriantodt.lin.ast.node.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class EnsureNotNullExpr(val value: Expr, override val section: Section? = null) : Expr {
    /* @automation(ast.impl EnsureNotNullExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitEnsureNotNullExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitEnsureNotNullExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitEnsureNotNullExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitEnsureNotNullExpr(this, param0)
    }
    /* @automation-end */
}
