package io.github.cafeteriaguild.lin.rt.types.properties

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.types.LType

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