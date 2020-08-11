package io.github.cafeteriaguild.lin.rt.lib.lang

import io.github.cafeteriaguild.lin.rt.lib.LObj

data class LString(val value: String) : LObj {
    override fun toString() = value

    operator fun plus(other: Any): LString {
        return LString(value + other)
    }
}