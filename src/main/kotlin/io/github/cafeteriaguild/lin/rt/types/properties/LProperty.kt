package io.github.cafeteriaguild.lin.rt.types.properties

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Represents a Lin property
 */
interface LProperty : LBaseProperty {
    val isSet: Boolean

    fun get(): LObj
    fun set(value: LObj)
}