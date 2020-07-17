package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LFloat(val value: Float) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Float"
    }

    override fun toString() = value.toString()
}