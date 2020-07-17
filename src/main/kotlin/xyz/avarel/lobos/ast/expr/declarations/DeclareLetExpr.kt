package xyz.avarel.lobos.ast.expr.declarations

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.patterns.PatternAST
import xyz.avarel.lobos.lexer.Section

class DeclareLetExpr(
    val pattern: PatternAST,
    val value: Expr,
    section: Section
) : AbstractExpr(section), LetExpr {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}