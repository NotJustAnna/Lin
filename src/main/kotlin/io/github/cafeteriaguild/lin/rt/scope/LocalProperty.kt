package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LocalProperty(val mutable: Boolean) : Property {
    private var field: LObj? = null
    override val getAllowed: Boolean
        get() = this.field != null
    override val setAllowed: Boolean
        get() = !getAllowed || mutable

    override fun get(): LObj {
        return field ?: throw LinException("Property not set.")
    }

    override fun set(value: LObj) {
        if (!setAllowed) throw LinException("Property can't be set.")
        field = value
    }
}