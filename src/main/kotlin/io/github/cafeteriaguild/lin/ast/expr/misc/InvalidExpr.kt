package io.github.cafeteriaguild.lin.ast.expr.misc

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.AbstractExpr
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.ExprParamVisitor
import io.github.cafeteriaguild.lin.ast.expr.ExprVisitor

class InvalidExpr(section: Section, val children: List<Expr>, val errors: List<Exception>) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)

    class Builder() {
        var section: Section? = null
        val children = mutableListOf<Expr>()
        val errors = mutableListOf<Exception>()

        fun section(value: Section) = apply {
            section = value
        }

        fun child(vararg values: Expr) = apply {
            children += values
            if (section == null && values.isNotEmpty()) section = values.first().section
        }

        fun error(vararg values: Exception) = apply {
            errors += values
        }


    }

    companion object {
        operator fun invoke(block: Builder.() -> Unit) = Builder().apply(block).build()
        private fun Builder.build() = InvalidExpr(section ?: error("Section wasn't set!"), children, errors)
    }
}
