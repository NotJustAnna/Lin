package io.github.cafeteriaguild.lin.rt.lib

data class LLong(override val value: Long) : LNumber {
    override fun toString() = value.toString()
}