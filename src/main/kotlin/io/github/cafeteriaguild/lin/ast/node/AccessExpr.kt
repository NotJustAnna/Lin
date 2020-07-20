package io.github.cafeteriaguild.lin.ast.node

import io.github.cafeteriaguild.lin.ast.node.ops.AccessResolver

/**
 * Marker interface for nodes that refer to variables.
 */
interface AccessExpr : Expr {
    fun <T, R> resolve(visitor: AccessResolver<T, R>, param: T): R
}