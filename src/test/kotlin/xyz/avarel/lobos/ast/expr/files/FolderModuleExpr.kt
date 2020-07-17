package xyz.avarel.lobos.ast.expr.files

import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.lexer.Section

class FolderModuleExpr(
    val name: String,
    val folderModules: List<FolderModuleExpr>,
    val fileModules: List<FileModuleExpr>,
    section: Section
) : AbstractExpr(section) {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
}