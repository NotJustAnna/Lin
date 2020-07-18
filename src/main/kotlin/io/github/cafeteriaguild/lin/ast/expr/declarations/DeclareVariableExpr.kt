package io.github.cafeteriaguild.lin.ast.expr.declarations

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.AbstractExpr
import io.github.cafeteriaguild.lin.ast.expr.ExprParamVisitor
import io.github.cafeteriaguild.lin.ast.expr.ExprVisitor
import io.github.cafeteriaguild.lin.ast.expr.Node

class DeclareVariableExpr(
    val name: String, val mutable: Boolean, val value: Node?, section: Section
) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}