package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LBoolean(val value: Boolean) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Boolean"
    }

    override fun toString() = value.toString()
}