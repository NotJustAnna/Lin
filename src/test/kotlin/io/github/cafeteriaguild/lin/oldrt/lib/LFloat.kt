package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass

class LFloat(val value: Float) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Float"
    }

    override fun toString() = value.toString()
}