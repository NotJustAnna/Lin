package io.github.cafeteriaguild.lin.rt.lib.lang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber

data class LChar(val value: Char) : LObj {
    override fun toString() = value.toString()

    operator fun plus(other: LNumber): LChar {
        return LChar(value + other.value.toInt())
    }

    operator fun plus(other: Any): LString {
        return LString(value.toString() + other)
    }
}