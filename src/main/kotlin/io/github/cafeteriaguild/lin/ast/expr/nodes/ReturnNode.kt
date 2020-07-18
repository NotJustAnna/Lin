package io.github.cafeteriaguild.lin.ast.expr.nodes

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.expr.AbstractExpr
import io.github.cafeteriaguild.lin.ast.expr.ExprParamVisitor
import io.github.cafeteriaguild.lin.ast.expr.ExprVisitor
import io.github.cafeteriaguild.lin.ast.expr.Node

class ReturnNode(val value: Node, section: Section) : AbstractExpr(section), Node {
    // NOTE by NotJustAnna:
    // "return" and "throw" is a Node, not an Expr.
    // You can val a = return b

    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: ExprParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}