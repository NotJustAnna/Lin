package io.github.cafeteriaguild.lin.oldrt.types.properties

import io.github.cafeteriaguild.lin.oldrt.types.LType
import io.github.cafeteriaguild.lin.rt.lib.LObj

class LocalProperty(override val type: LType, override val name: String, override val mutable: Boolean) : LProperty {
    private var field: LObj? = null

    override val isSet: Boolean
        get() = this.field != null

    override fun get(): LObj {
        return field ?: throw IllegalStateException("Property not set.")
    }

    override fun set(value: LObj) {
        if (!mutable && isSet) throw IllegalStateException("Property is immutable and cannot be set again.")
        field = value
    }
}