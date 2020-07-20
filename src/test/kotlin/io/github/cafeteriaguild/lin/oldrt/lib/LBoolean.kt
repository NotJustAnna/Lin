package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LBoolean(val value: Boolean) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Boolean"
    }

    override fun toString() = value.toString()
}