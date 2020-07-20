package io.github.cafeteriaguild.lin.ast.node.nodes

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.AbstractNode
import io.github.cafeteriaguild.lin.ast.node.AccessExpr
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor
import io.github.cafeteriaguild.lin.ast.node.ops.AccessResolver


class IdentifierExpr(val name: String, section: Section) : AbstractNode(section), AccessExpr {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
    override fun <T, R> resolve(visitor: AccessResolver<T, R>, param: T) = visitor.resolve(this, param)
}