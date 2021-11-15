package net.notjustanna.lin.parser

public object Precedence {
    /* a++ | a-- | a.b | a?.b | A? | a(b) | a[b] | a!! */
    public const val POSTFIX: Int = 15

    /* -a | +a | ++a | --a | !a */
    public const val PREFIX: Int = 14

    /* : A | b as A | b as? A */
    public const val TYPE_RHS: Int = 13

    /* a * b | a / b | a % b */
    public const val MULTIPLICATIVE: Int = 12

    /* a + b | a - b */
    public const val ADDITIVE: Int = 11

    /* a..b */
    public const val RANGE: Int = 10

    /* a to b */
    public const val INFIX: Int = 9

    /* a ?: b */
    public const val ELVIS: Int = 8

    /* a in b | a !in b | a is b | a !is b */
    public const val NAMED_CHECKS: Int = 7

    /* a > b | a < b | a >= b | a <= b */
    public const val COMPARISON: Int = 6

    /* a == b | a != b */
    public const val EQUALITY: Int = 5

    /* a && b */
    public const val CONJUNCTION: Int = 4

    /* a || b */
    public const val DISJUNCTION: Int = 3

    /* *array */
    public const val SPREAD_OPERATOR: Int = 2

    /* a = b | a += b | a -= b | a *= b | a /= b | a %= b */
    public const val ASSIGNMENT: Int = 1
}
