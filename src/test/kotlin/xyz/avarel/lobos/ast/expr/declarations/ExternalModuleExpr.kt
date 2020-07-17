package xyz.avarel.lobos.ast.expr.declarations

import xyz.avarel.lobos.ast.ExternalDeclarationsAST
import xyz.avarel.lobos.ast.expr.AbstractExpr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.lexer.Section

class ExternalModuleExpr(
    val name: String,
    val declarationsAST: ExternalDeclarationsAST,
    section: Section
) : AbstractExpr(section), ModuleExpr {
    override fun <R> accept(visitor: ExprVisitor<R>) = visitor.visit(this)
    override fun isEmpty() = declarationsAST.isEmpty()
}
