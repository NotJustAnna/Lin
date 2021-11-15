package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

public data class BooleanExpr(val value: Boolean, override val section: Section? = null) : ConstExpr {
    /* @automation(ast.impl BooleanExpr,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitBooleanExpr(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitBooleanExpr(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitBooleanExpr(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitBooleanExpr(this, param0)
    }
    /* @automation-end */
}
