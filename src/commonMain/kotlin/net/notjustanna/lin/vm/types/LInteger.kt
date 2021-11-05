package net.notjustanna.lin.vm.types

data class LInteger(val value: Long) : LNumber() {
    override fun compareTo(other: LNumber): Int {
        return when (other) {
            is LDecimal -> value.compareTo(other.value)
            is LInteger -> value.compareTo(other.value)
        }
    }

    override fun plus(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value + right.value)
            is LInteger -> LInteger(value + right.value)
        }
    }

    override fun minus(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value - right.value)
            is LInteger -> LInteger(value - right.value)
        }
    }

    override fun times(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value * right.value)
            is LInteger -> LInteger(value * right.value)
        }
    }

    override fun div(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value / right.value)
            is LInteger -> LInteger(value / right.value)
        }
    }

    override fun rem(right: LNumber): LNumber {
        return when (right) {
            is LDecimal -> LDecimal(value % right.value)
            is LInteger -> LInteger(value % right.value)
        }
    }

    override fun unaryPlus(): LNumber {
        return LInteger(+value)
    }

    override fun unaryMinus(): LNumber {
        return LInteger(-value)
    }

    override fun truth(): Boolean {
        return value != 0L
    }

    override val linType: String
        get() = "integer"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return value.toString()
    }

    operator fun rangeTo(right: LInteger): LAny {
        return LRange(value..right.value)
    }
}
