package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section

class BinaryOperation(
    val left: Expr, val right: Expr, val operator: BinaryOperationType, override val section: Section
) : Expr

enum class BinaryOperationType {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, REMAINING, EQUALS, NOT_EQUALS, AND, OR, LT, LTE, GT, GTE, ELVIS, IN, IS, RANGE
}

class UnaryOperation(val target: Expr, val operator: UnaryOperationType, override val section: Section) : Expr

enum class UnaryOperationType {
    POSITIVE, NEGATIVE, NOT,
}
