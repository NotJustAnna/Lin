package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

data class NullExpr(override val section: Section) : Expr

data class UnitExpr(override val section: Section) : Expr

data class BooleanExpr(val value: Boolean, override val section: Section) : Expr

data class IntExpr(val value: Int, override val section: Section) : Expr

data class LongExpr(val value: Long, override val section: Section) : Expr

data class FloatExpr(val value: Float, override val section: Section) : Expr

data class DoubleExpr(val value: Double, override val section: Section) : Expr

data class CharExpr(val value: Char, override val section: Section) : Expr

data class StringExpr(val value: String, override val section: Section) : Expr

data class ArrayExpr(val value: List<Expr>, override val section: Section) : Expr

data class ObjectExpr(val value: List<Pair<Expr, Expr>>, override val section: Section) : Expr

data class ThisExpr(override val section: Section) : Expr

data class FunctionExpr(
    val parameters: List<Parameter>,
    val name: String?,
    val body: Node?,
    override val section: Section
): Expr {
    data class Parameter(val name: String, val varargs: Boolean, val defaultValue: Expr?)
}
