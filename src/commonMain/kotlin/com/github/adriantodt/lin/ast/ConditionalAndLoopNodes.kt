package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

data class IfExpr(
    val condition: Expr,
    val thenBranch: Expr,
    val elseBranch: Expr,
    override val section: Section
) : Expr

data class IfNode(
    val condition: Expr,
    val thenBranch: Node,
    val elseBranch: Node?,
    override val section: Section
) : Node

data class DoWhileNode(val body: Node?, val condition: Expr, override val section: Section) : Node
data class WhileNode(val condition: Expr, val body: Node?, override val section: Section) : Node
