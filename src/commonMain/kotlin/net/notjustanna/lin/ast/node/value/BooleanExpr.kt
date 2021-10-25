package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class BooleanExpr(val value: Boolean, override val section: Section) : Expr {
    /* @automation(ast.impl BooleanExpr)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitBooleanExpr(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitBooleanExpr(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitBooleanExpr(this, param0)
    /* @automation-end */
}
