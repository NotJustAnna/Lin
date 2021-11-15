package net.notjustanna.lin.ast.node.control

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

/*
 * NOTE: TryExpr is one of those dynamic expressions which may explicitly return `null`
 * and be an expression, even though the branch had an regular node.
 */

public data class TryExpr(
    val tryBranch: Node,
    val catchBranch: CatchBranch?,
    val finallyBranch: Node?,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl TryExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitTryExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitTryExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitTryExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitTryExpr(this, param0)
    }
    /* @automation-end */
}


