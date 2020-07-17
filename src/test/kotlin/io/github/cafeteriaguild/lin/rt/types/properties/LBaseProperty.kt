package io.github.cafeteriaguild.lin.rt.types.properties

import io.github.cafeteriaguild.lin.rt.types.LType

/**
 * The base of all Lin properties.
 */
interface LBaseProperty {
    val type: LType
    val name: String
    val mutable: Boolean
}