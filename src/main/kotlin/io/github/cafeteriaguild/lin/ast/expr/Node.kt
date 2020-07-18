package io.github.cafeteriaguild.lin.ast.expr

/**
 * Interface for expressions that generates result when interpreted.
 *
 * (Note: Expressions that doesn't implement this interface are expected to generates an "Unit" or void result.)
 */
interface Node : Expr {
}