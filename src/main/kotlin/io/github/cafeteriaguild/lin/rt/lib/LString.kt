package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LString(val value: String) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.String"
    }

    override fun toString() = value.toString()
}