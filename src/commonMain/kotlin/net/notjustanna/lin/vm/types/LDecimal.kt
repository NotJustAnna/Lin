package net.notjustanna.lin.vm.types

public data class LDecimal(val value: Double) : LNumber() {
    override fun compareTo(other: LNumber): Int {
        return when (other) {
            is LDecimal -> value.compareTo(other.value)
            is LInteger -> value.compareTo(other.value)
        }
    }

    override fun plus(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value + right.value)
            is LInteger -> LDecimal(value + right.value)
        }
    }

    override fun minus(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value - right.value)
            is LInteger -> LDecimal(value - right.value)
        }
    }

    override fun times(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value * right.value)
            is LInteger -> LDecimal(value * right.value)
        }
    }

    override fun div(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value / right.value)
            is LInteger -> LDecimal(value / right.value)
        }
    }

    override fun rem(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value % right.value)
            is LInteger -> LDecimal(value % right.value)
        }
    }

    override fun unaryPlus(): LNumber {
        return LDecimal(+value)
    }

    override fun unaryMinus(): LNumber {
        return LDecimal(-value)
    }

    override fun truth(): Boolean {
        return value != 0.0
    }

    override val linType: String
        get() = "decimal"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return value.toString()
    }
}
