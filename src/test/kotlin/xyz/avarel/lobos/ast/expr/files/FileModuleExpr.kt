package xyz.avarel.lobos.ast.expr.files

import xyz.avarel.lobos.ast.DeclarationsAST
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.expr.declarations.DeclareModuleExpr
import xyz.avarel.lobos.lexer.Section

class FileModuleExpr(
    name: String,
    declarationsAST: DeclarationsAST,
    section: Section
) : DeclareModuleExpr(name, declarationsAST, section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}