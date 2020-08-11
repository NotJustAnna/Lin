package io.github.cafeteriaguild.lin.rt.lib.nativelang.properties

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LazyProperty(block: () -> LObj) : Property {
    private val field: LObj by lazy(block)
    override val getAllowed: Boolean
        get() = true
    override val setAllowed: Boolean
        get() = false

    override fun get(): LObj {
        return field
    }

    override fun set(value: LObj) {
        throw LinException("Property can't be set.")
    }
}