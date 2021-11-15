package net.notjustanna.lin.ast.node.declare

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.value.FunctionExpr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class DeclareFunctionExpr(
    val name: String,
    val value: FunctionExpr,
    override val section: Section? = null
) : Expr {
    /* @automation(ast.impl DeclareFunctionExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitDeclareFunctionExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitDeclareFunctionExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitDeclareFunctionExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitDeclareFunctionExpr(this, param0)
    }
    /* @automation-end */
}
