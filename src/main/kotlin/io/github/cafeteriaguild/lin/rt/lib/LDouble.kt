package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LDouble(val value: Double) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Double"
    }

    override fun toString() = value.toString()
}