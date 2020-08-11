package io.github.cafeteriaguild.lin.rt.lib.nativelang.properties

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj

class GetterProperty(val block: () -> LObj) : Property {
    override val getAllowed: Boolean
        get() = true
    override val setAllowed: Boolean
        get() = false

    override fun get(): LObj {
        return block()
    }

    override fun set(value: LObj) {
        throw LinException("Property can't be set.")
    }
}