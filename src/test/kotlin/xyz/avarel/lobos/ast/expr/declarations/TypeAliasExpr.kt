package xyz.avarel.lobos.ast.expr.declarations

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.types.GenericParameterAST
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.lexer.Section

class TypeAliasExpr(
    val name: String,
    val generics: List<GenericParameterAST>,
    val type: TypeAST,
    section: Section
) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}