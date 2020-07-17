package xyz.avarel.lobos.ast.expr.misc

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.lexer.Section

class IfExpr(
    val condition: Expr,
    val thenBranch: Expr,
    val elseBranch: Expr?,
    section: Section
) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}
