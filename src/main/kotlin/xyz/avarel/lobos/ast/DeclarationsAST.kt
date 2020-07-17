package xyz.avarel.lobos.ast

import xyz.avarel.lobos.ast.expr.declarations.*

class DeclarationsAST(
    val uses: MutableList<UseExpr> = mutableListOf(),
    val modules: MutableList<ModuleExpr> = mutableListOf(),
    val functions: MutableList<FunctionExpr> = mutableListOf(),
    val variables: MutableList<LetExpr> = mutableListOf()
) {
    fun isEmpty() = modules.isEmpty() && functions.isEmpty() && variables.isEmpty()
}

class ExternalDeclarationsAST(
    val uses: MutableList<UseExpr> = mutableListOf(),
    val modules: MutableList<ExternalModuleExpr> = mutableListOf(),
    val functions: MutableList<ExternalFunctionExpr> = mutableListOf(),
    val variables: MutableList<ExternalLetExpr> = mutableListOf()
) {
    fun isEmpty() = modules.isEmpty() && functions.isEmpty() && variables.isEmpty()
}