package io.github.cafeteriaguild.lin.oldrt.types

import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction
import io.github.cafeteriaguild.lin.oldrt.types.properties.LObjectProperty

/**
 * Represents a Lin interface
 */
interface LTInterface : LType {
    abstract class Base : LTInterface {
        override val name: String? = null
        override val superType: LTClass? = null
        override val interfaces: List<LTInterface> = emptyList()
        override val memberFunctions: Map<String, LFunction> = emptyMap()
        override val memberProperties: Map<String, LObjectProperty> = emptyMap()

        override fun hashCode(): Int {
            return name.hashCode() xor superType.hashCode() xor interfaces.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is LType) return false
            return name == other.name && superType == other.superType && interfaces == other.interfaces
        }
    }

    data class Simple(
        override val name: String? = null,
        override val superType: LTClass? = null,
        override val interfaces: List<LTInterface> = emptyList(),
        override val memberFunctions: Map<String, LFunction> = emptyMap(),
        override val memberProperties: Map<String, LObjectProperty> = emptyMap()
    ) : LTInterface {
        override fun hashCode(): Int {
            return name.hashCode() xor superType.hashCode() xor interfaces.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is LType) return false
            return name == other.name && superType == other.superType && interfaces == other.interfaces
        }
    }
}