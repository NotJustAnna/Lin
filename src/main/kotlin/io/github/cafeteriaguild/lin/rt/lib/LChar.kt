package io.github.cafeteriaguild.lin.rt.lib

data class LChar(val value: Char) : LObj {
    override fun toString() = value.toString()

    operator fun plus(other: Any): LString {
        return LString(value.toString() + other)
    }
}