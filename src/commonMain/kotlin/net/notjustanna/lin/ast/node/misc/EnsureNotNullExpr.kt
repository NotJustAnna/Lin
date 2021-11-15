package net.notjustanna.lin.ast.node.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

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
