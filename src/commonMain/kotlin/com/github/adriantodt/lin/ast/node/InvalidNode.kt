package com.github.adriantodt.lin.ast.node

import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.lin.ast.node.Expr

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

    /* @automation(ast.impl InvalidNode,Expr)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitInvalidNode(this)

    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitInvalidNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitInvalidNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitInvalidNode(this, param0)
    /* @automation-end */

    companion object {
        operator fun invoke(block: Builder.() -> Unit) = Builder().apply(block).build()
        private fun Builder.build() = InvalidNode(section ?: error("Section wasn't set!"), children, errors)
    }
}
