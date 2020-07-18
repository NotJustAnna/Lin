package io.github.cafeteriaguild.lin.ast.expr.misc

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class IfExpr(
    val condition: Node,
    val thenBranch: Expr,
    val elseBranch: Expr?,
    section: Section
) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}
