package xyz.avarel.lobos.ast.expr.misc

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ExprVisitor

class MultiExpr(val list: List<Expr>) : AbstractExpr(list.first().section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}
