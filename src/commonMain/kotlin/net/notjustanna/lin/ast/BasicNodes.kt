package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional

/**
 * This is a simple AST node from Lin.
 * Nodes that don't extend [Expr] are meant to have no value.
 * If you have to extract a value from a Node which doesn't extend [Expr], consider it's value `unit`.
 */
sealed interface Node : Sectional

/**
 * This is an AST expression node from Lin.
 * This expression node will always compute to a value.
 */
sealed interface Expr : Node

/**
 * This represents a given list of nodes.
 * Nodes must be executed sequentially.
 * This AST node's main use is function bodies and main scopes.
 */
data class MultiNode(val list: List<Node>, override val section: Section) : Node

/**
 * This represents a given list of nodes, with the last node being necessarily an expression.
 * Nodes must be executed sequentially. The value of this expression is the last expression's value.
 * This AST node's main use is lambda bodies and REPL scopes.
 */
data class MultiExpr(val list: List<Node>, val last: Expr, override val section: Section) : Expr
