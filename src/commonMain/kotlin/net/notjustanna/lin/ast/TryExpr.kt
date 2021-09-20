package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

/*
 * NOTE: TryExpr is one one of those dynamic expressions which may explicitly return `unit`
 * and be an expression, even though the branch had an regular node.
 */

data class TryExpr(
    val tryBranch: Node,
    val catchBranch: CatchNode?,
    val finallyBranch: Node?,
    override val section: Section
) : Expr

data class CatchNode(
    val caughtName: String, val branch: Node
)

