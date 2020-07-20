package io.github.cafeteriaguild.lin.rt.lib

data class LFloat(override val value: Float) : LNumber {
    override fun toString() = value.toString()
}