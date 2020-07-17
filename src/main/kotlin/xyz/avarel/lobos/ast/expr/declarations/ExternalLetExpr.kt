package xyz.avarel.lobos.ast.expr.declarations

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.lexer.Section

class ExternalLetExpr(
    val mutable: Boolean,
    val name: String,
    val type: TypeAST,
    section: Section
) : AbstractExpr(section), LetExpr {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}