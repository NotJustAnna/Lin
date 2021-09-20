package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

/*
 * NOTE: `return`, `throw`, `break` and `continue` are expressions, since you can:
 * - `val a = maybeNull ?: return b`
 * - `val a = maybeNull ?: throw b`
 * - `val a = maybeNull ?: break`
 * - `val a = maybeNull ?: continue`
 */

data class ReturnExpr(val value: Expr, override val section: Section) : Expr

data class ThrowExpr(val value: Expr, override val section: Section) : Expr

data class BreakExpr(override val section: Section) : Expr

data class ContinueExpr(override val section: Section) : Expr
