package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass

class LString(val value: String) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.String"
    }

    override fun toString() = value.toString()
}