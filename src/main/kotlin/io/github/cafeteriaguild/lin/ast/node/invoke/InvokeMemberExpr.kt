package io.github.cafeteriaguild.lin.ast.node.invoke

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.AbstractNode
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor

class InvokeMemberExpr(
    val target: Expr, val nullSafe: Boolean, val name: String, val arguments: List<Expr>,
    section: Section
) : AbstractNode(section), Expr {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}