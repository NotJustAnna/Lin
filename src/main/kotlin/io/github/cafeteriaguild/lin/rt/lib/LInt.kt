package io.github.cafeteriaguild.lin.rt.lib

data class LInt(override val value: Int) : LNumber {
    override fun toString() = value.toString()
}