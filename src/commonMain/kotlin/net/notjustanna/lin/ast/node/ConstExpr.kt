package net.notjustanna.lin.ast.node

/**
 * This is an AST constant expression node from Lin.
 * This expression node will always compute to a value.
 * This is a marker used to indicate if a given Node can be optimized away.
 */
public interface ConstExpr : Expr
