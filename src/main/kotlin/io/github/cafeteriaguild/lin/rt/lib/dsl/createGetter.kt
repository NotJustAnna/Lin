package io.github.cafeteriaguild.lin.rt.lib.dsl

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.scope.Property

fun createGetter(block: () -> LObj): Property {
    return object : Property {
        override val getAllowed: Boolean
            get() = true
        override val setAllowed: Boolean
            get() = false

        override fun get(): LObj {
            return block()
        }

        override fun set(value: LObj) {
            throw LinException("Set not allowed")
        }
    }
}