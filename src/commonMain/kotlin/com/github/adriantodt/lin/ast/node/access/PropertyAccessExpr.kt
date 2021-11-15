package com.github.adriantodt.lin.ast.node.access

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class PropertyAccessExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl PropertyAccessExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitPropertyAccessExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitPropertyAccessExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitPropertyAccessExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitPropertyAccessExpr(this, param0)
    }
    /* @automation-end */
}
