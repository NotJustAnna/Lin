package io.github.cafeteriaguild.lin.ast.node.misc

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.*

class TryExpr(
    val tryBranch: Node,
    val catchBranch: CatchBranch?,
    val finallyBranch: Node?,
    section: Section
) : AbstractNode(section), Expr {
    data class CatchBranch(
        val caughtName: String, val branch: Node
    )

    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}
