package com.github.adriantodt.lin.ast.node

import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class InvalidNode(override val section: Section?, val children: List<Node>, val errors: List<Exception>) :
    Expr {

    public class Builder {
        public var section: Section? = null
        public val children: MutableList<Node> = mutableListOf()
        public val errors: MutableList<Exception> = mutableListOf()

        public fun section(value: Section?): Builder {
            if (value != null) {
                section = value
            }
            return this
        }

        public fun child(vararg values: Node?): Builder {
            val list = values.filterNotNull()
            children += list
            if (section == null && list.isNotEmpty()) section = list.first().section
            return this
        }

        public fun error(vararg values: Exception): Builder {
            errors += values
            return this
        }
    }

    /* @automation(ast.impl InvalidNode,Expr)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitInvalidNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Expr {
        return visitor.visitInvalidNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitInvalidNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitInvalidNode(this, param0)
    }
    /* @automation-end */

    public companion object {
        public operator fun invoke(block: Builder.() -> Unit): InvalidNode = Builder().apply(block).build()
        private fun Builder.build() = InvalidNode(section, children, errors)
    }
}
