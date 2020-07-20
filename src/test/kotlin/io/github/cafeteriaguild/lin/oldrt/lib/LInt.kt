package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass

class LInt(val value: Int) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Int"
    }

    override fun toString() = value.toString()
}