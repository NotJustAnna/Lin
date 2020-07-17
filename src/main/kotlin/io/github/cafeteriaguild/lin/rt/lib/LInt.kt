package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LInt(val value: Int) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Int"
    }

    override fun toString() = value.toString()
}