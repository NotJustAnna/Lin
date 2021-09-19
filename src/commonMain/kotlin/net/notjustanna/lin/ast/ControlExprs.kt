package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

data class ThisExpr(override val section: Section) : Expr

data class IdentifierExpr(val name: String, override val section: Section) : Expr

data class EnsureNotNullExpr(val value: Expr, override val section: Section) : Expr

/*
 * NOTE: "return" and "throw" are expressions, since you can `val a = return b`
 */

data class ReturnExpr(val value: Expr, override val section: Section) : Expr

data class ThrowExpr(val value: Expr, override val section: Section) : Expr

data class BreakExpr(override val section: Section) : Expr
