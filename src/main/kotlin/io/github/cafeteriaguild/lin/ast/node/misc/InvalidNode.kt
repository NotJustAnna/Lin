package io.github.cafeteriaguild.lin.ast.node.misc

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.AbstractNode
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor

class InvalidNode(section: Section, val children: List<Node>, val errors: List<Exception>) : AbstractNode(section) {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)

    class Builder() {
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

    companion object {
        operator fun invoke(block: Builder.() -> Unit) = Builder().apply(block).build()
        private fun Builder.build() = InvalidNode(section ?: error("Section wasn't set!"), children, errors)
    }
}
