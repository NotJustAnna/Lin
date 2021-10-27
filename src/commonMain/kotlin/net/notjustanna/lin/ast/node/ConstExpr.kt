package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeMapVisitor

/**
 * This is an AST constant expression node from Lin.
 * This expression node will always compute to a value.
 * This is a marker used to indicate if a given Node can be optimized away.
 */
interface ConstExpr : Expr {
    /* @automation(ast.override ConstExpr,Expr)-start */
    override fun accept(visitor: NodeMapVisitor): ConstExpr
    /* @automation-end */
}
