package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

class LLong(val value: Long) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Long"
    }

    override fun toString() = value.toString()
}