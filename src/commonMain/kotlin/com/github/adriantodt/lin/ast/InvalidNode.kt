package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

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

    companion object {
        operator fun invoke(block: Builder.() -> Unit) = Builder().apply(block).build()
        private fun Builder.build() = InvalidNode(section ?: error("Section wasn't set!"), children, errors)
    }
}
