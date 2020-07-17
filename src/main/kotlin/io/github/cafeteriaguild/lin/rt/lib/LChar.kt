package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LChar(val value: Char) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Char"
    }

    override fun toString() = value.toString()
}