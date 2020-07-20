package io.github.cafeteriaguild.lin.oldrt.types.properties

import io.github.cafeteriaguild.lin.rt.lib.LObj

class BoundProperty(val property: LObjectProperty, val target: LObj) : LProperty, LBaseProperty by property {

    override val isSet: Boolean
        get() = property.isSet(target)

    override fun get(): LObj {
        return property.get(target)
    }

    override fun set(value: LObj) {
        property.set(target, value)
    }
}