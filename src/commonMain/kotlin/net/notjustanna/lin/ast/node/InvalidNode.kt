package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeVisitor0
import net.notjustanna.lin.ast.visitor.NodeVisitor0R
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.tartar.api.lexer.Section

data class InvalidNode(override val section: Section, val children: List<Node>, val errors: List<Exception>) : Expr {

    class Builder {
        var section: Section? = null
        val children = mutableListOf<Node>()
        val errors = mutableListOf<Exception>()

        fun section(value: Section) = apply {
            section = value
        }

        fun child(vararg values: Node?) = apply {
            val list = values.filterNotNull()
            children += list
            if (section == null && list.isNotEmpty()) section = list.first().section
        }

        fun error(vararg values: Exception) = apply {
            errors += values
        }
    }

    /* @automation(ast.impl InvalidNode)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitInvalidNode(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitInvalidNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvalidNode(this, param0)
    /* @automation-end */

    companion object {
        operator fun invoke(block: Builder.() -> Unit) = Builder().apply(block).build()
        private fun Builder.build() = InvalidNode(section ?: error("Section wasn't set!"), children, errors)
    }
}
