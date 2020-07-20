package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LLong(val value: Long) : LObj(Type) {
    object Type : LTClass.Base() {
        override val name = "lin.Long"
    }

    override fun toString() = value.toString()
}