package io.github.cafeteriaguild.lin.ast.node.ops

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.node.*

class PreAssignUnaryOperation(
    val target: AccessExpr,
    val operator: UnaryAssignOperationType,
    section: Section
) : AbstractNode(section), Expr {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}