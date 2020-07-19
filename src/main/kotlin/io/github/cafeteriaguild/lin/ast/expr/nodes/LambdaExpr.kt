package io.github.cafeteriaguild.lin.ast.expr.nodes

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class LambdaExpr(
    val parameters: List<Parameter>,
    val body: Node,
    section: Section
) : AbstractNode(section), Expr {
    sealed class Parameter {
        class Destructured(val names: List<String>) : Parameter() {
            override fun toString() = names.joinToString(", ", "(", ")")
        }

        class Named(val name: String) : Parameter() {
            override fun toString() = name
        }
    }

    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}