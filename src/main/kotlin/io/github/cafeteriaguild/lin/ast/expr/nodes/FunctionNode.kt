package io.github.cafeteriaguild.lin.ast.expr.nodes

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class FunctionNode(
    val parameters: List<Parameter>,
    val body: Expr,
    section: Section
) : AbstractExpr(section), Node {
    data class Parameter(
        val name: String,
        val value: Node?
    )

    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}