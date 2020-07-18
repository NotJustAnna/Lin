package io.github.cafeteriaguild.lin.ast.expr.access

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class PropertyAccessNode(
    val target: Node, val nullSafe: Boolean, val name: String, section: Section
) : AbstractExpr(section), AccessNode {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}