package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LDouble(val value: Double) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Double"
    }

    override fun toString() = value.toString()
}