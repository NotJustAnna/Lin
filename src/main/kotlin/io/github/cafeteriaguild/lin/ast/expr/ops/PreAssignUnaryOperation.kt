package io.github.cafeteriaguild.lin.ast.expr.ops

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class PreAssignUnaryOperation(
    val target: AccessNode,
    val operator: UnaryAssignOperationType,
    section: Section
) : AbstractExpr(section), Node {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}