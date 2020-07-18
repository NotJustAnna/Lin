package io.github.cafeteriaguild.lin.parser

object Precedence {
    /* a++ | a-- | a.b | a?.b | A? | a(b) | a[b] | a!! */
    const val POSTFIX = 15

    /* -a | +a | ++a | --a | !a */
    const val PREFIX = 14

    /* : A | b as A | b as? A */
    const val TYPE_RHS = 13

    /* a * b | a / b | a % b */
    const val MULTIPLICATIVE = 12

    /* a + b | a - b */
    const val ADDITIVE = 11

    /* a..b */
    const val RANGE = 10

    /* a to b */
    const val INFIX = 9

    /* a ?: b */
    const val ELVIS = 8

    /* a in b | a !in b | a is b | a !is b */
    const val NAMED_CHECKS = 7

    /* a > b | a < b | a >= b | a <= b */
    const val COMPARISON = 6

    /* a == b | a != b */
    const val EQUALITY = 5

    /* a && b */
    const val CONJUNCTION = 4

    /* a || b */
    const val DISJUNCTION = 3

    /* *array */
    const val SPREAD_OPERATOR = 2

    /* a = b | a += b | a -= b | a *= b | a /= b | a %= b */
    const val ASSIGNMENT = 1
}