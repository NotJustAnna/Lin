package io.github.cafeteriaguild.lin.oldrt.types.properties

import io.github.cafeteriaguild.lin.oldrt.lib.LObj
import io.github.cafeteriaguild.lin.oldrt.types.LType

open class InObjectProperty(
    override val name: String, override val type: LType, override val mutable: Boolean
) : LObjectProperty {

    override fun isSet(target: LObj): Boolean = target.propertyValues.contains(name)

    override fun get(target: LObj): LObj {
        return target.propertyValues[name] ?: throw IllegalStateException("Property not set.")
    }

    override fun set(target: LObj, value: LObj) {
        if (!mutable && isSet(target)) throw IllegalStateException("Property is immutable and cannot be set again.")
        target.propertyValues[name] = value
    }
}