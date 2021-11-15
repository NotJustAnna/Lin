package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeMapVisitor

/**
 * This is an AST expression node from Lin.
 * This expression node will always compute to a value.
 */
public interface Expr : Node {
    /* @automation(ast.override Expr,Node)-start */
    override fun accept(visitor: NodeMapVisitor): Expr
    /* @automation-end */
}
