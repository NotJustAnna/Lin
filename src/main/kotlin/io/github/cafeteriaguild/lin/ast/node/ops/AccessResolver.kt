package io.github.cafeteriaguild.lin.ast.node.ops

import io.github.cafeteriaguild.lin.ast.node.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.node.access.SubscriptAccessExpr
import io.github.cafeteriaguild.lin.ast.node.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.ast.node.nodes.ThisExpr

interface AccessResolver<T, R> {
    fun resolve(node: ThisExpr, param: T): R
    fun resolve(node: IdentifierExpr, param: T): R
    fun resolve(node: PropertyAccessExpr, param: T): R
    fun resolve(node: SubscriptAccessExpr, param: T): R
}