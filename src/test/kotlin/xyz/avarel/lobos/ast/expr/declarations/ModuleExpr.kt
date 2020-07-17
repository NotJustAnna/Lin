package xyz.avarel.lobos.ast.expr.declarations

import xyz.avarel.lobos.ast.expr.Expr

interface ModuleExpr : Expr {
    fun isEmpty(): Boolean
}