package io.github.cafeteriaguild.lin.rt.types.properties

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Represents a Lin object property
 */
interface LObjectProperty : LBaseProperty {
    fun isSet(target: LObj): Boolean

    fun get(target: LObj): LObj
    fun set(target: LObj, value: LObj)

    fun bind(target: LObj): LProperty = BoundProperty(this, target)

}