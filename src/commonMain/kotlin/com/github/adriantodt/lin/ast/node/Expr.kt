package com.github.adriantodt.lin.ast.node

import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor

/**
 * This is an AST expression node from Lin.
 * This expression node will always compute to a value.
 */
interface Expr : Node {
    /* @automation(ast.override Expr,Node)-start */
    override fun accept(visitor: NodeMapVisitor): Expr
    /* @automation-end */
}
