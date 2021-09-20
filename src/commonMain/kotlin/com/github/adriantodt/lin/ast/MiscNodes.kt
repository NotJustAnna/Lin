package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

data class EnsureNotNullExpr(val value: Expr, override val section: Section) : Expr

data class TypeofExpr(val value: Expr, override val section: Section) : Expr
