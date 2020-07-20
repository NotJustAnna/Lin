package io.github.cafeteriaguild.lin.ast.node.declarations

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.AbstractNode
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor

class DestructuringVariableNode(
    val names: List<String>,
    val mutable: Boolean,
    val value: Expr,
    section: Section,
    val modifiers: Set<LinModifier> = emptySet()
) : AbstractNode(section) {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}