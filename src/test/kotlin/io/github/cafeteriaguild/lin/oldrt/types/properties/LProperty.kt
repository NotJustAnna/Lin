package io.github.cafeteriaguild.lin.oldrt.types.properties

import io.github.cafeteriaguild.lin.oldrt.lib.LObj

/**
 * Represents a Lin property
 */
interface LProperty : LBaseProperty {
    val isSet: Boolean

    fun get(): LObj
    fun set(value: LObj)
}