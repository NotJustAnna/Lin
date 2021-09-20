package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

data class IdentifierExpr(val name: String, override val section: Section) : Expr

data class AssignNode(val name: String, val value: Expr, override val section: Section) : Node

data class PropertyAccessExpr(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    override val section: Section
) : Expr

data class PropertyAssignNode(
    val target: Expr,
    val nullSafe: Boolean,
    val name: String,
    val value: Expr,
    override val section: Section
) : Node

data class SubscriptAccessExpr(
    val target: Expr,
    val arguments: List<Expr>,
    override val section: Section
) : Expr

data class SubscriptAssignNode(
    val target: Expr,
    val arguments: List<Expr>,
    val value: Expr,
    override val section: Section
) : Node
