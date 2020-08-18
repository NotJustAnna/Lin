package io.github.cafeteriaguild.lin.ast.node.misc

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.*

class ForNode(val variable: Variable, val iterable: Expr, val body: Node, section: Section) : AbstractNode(section) {
    sealed class Variable {
        class Destructured(val names: List<String>) : Variable() {
            override fun toString() = names.joinToString(", ", "(", ")")
        }

        class Named(val name: String) : Variable() {
            override fun toString() = name
        }
    }

    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}
