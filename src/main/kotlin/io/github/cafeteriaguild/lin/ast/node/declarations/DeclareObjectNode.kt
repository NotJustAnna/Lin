package io.github.cafeteriaguild.lin.ast.node.declarations

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.AbstractNode
import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor
import io.github.cafeteriaguild.lin.ast.node.nodes.ObjectExpr

class DeclareObjectNode(
    val name: String,
    val obj: ObjectExpr,
    section: Section,
    val modifiers: Set<LinModifier> = emptySet()
) : AbstractNode(section), Declaration {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}