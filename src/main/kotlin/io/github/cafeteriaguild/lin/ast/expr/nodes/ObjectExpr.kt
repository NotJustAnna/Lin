package io.github.cafeteriaguild.lin.ast.expr.nodes

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.*

class ObjectExpr(val body: List<Declaration>, section: Section) : AbstractNode(section), Expr {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}