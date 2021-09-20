package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

data class EnsureNotNullExpr(val value: Expr, override val section: Section) : Expr

data class TypeofExpr(val value: Expr, override val section: Section) : Expr
