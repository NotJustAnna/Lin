package io.github.cafeteriaguild.lin.rt.lib.lang.number

data class LDouble(override val value: Double) : LNumber {
    override fun toString() = value.toString()
}