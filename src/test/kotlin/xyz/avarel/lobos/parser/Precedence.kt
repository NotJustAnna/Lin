package xyz.avarel.lobos.parser

object Precedence {
    const val DOT = 14

    /* a(b) */
    const val POSTFIX = 13

    /* a ^ b */
    const val EXPONENTIAL = 11

    /* -a | +a | !a | ~a */
    const val PREFIX = 10

    /* a * b | a / b | a % b */
    const val MULTIPLICATIVE = 9

    /* a + b | a - b */
    const val ADDITIVE = 8

    const val SHIFT = 7

    /* a..b */
    const val RANGE_TO = 6

    /* Unused */
    const val INFIX = 5

    /* a > b | a < b | a >= b | a <= b */
    const val COMPARISON = 4

    /* a == b | a != b */
    const val EQUALITY = 3

    /* a && b */
    const val CONJUNCTION = 2

    /* a || b */
    const val DISJUNCTION = 1
}