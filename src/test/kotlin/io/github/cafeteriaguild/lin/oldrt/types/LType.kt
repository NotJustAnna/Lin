package io.github.cafeteriaguild.lin.oldrt.types

import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction
import io.github.cafeteriaguild.lin.oldrt.types.properties.LObjectProperty

/**
 * Represents a Lin type.
 *
 * For simplicity purposes:
 * - If named, LType should defer hashCode and equals to name.
 *
 */
interface LType {
    val name: String?
    val superType: LTClass?
    val interfaces: List<LTInterface>
    val memberFunctions: Map<String, LFunction>
    val memberProperties: Map<String, LObjectProperty>
}

