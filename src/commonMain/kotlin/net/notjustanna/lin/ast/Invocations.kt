package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

data class InvokeExpr(val target: Expr, val arguments: List<Expr>, override val section: Section) : Expr

data class InvokeLocalExpr(val name: String, val arguments: List<Expr>, override val section: Section) : Expr

data class InvokeMemberExpr(
    val target: Expr, val nullSafe: Boolean, val name: String, val arguments: List<Expr>, override val section: Section
) : Expr

data class InvokeExtensionExpr(
    val left: Expr, val nullSafe: Boolean, val right: Expr, val arguments: List<Expr>, override val section: Section
) : Expr

