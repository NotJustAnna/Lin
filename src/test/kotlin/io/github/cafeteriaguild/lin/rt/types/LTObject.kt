package io.github.cafeteriaguild.lin.rt.types

import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
import io.github.cafeteriaguild.lin.rt.types.properties.LObjectProperty

/**
 * Represents a Lin object
 */
interface LTObject : LType {
    abstract class Base : LTObject {
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

    data class Impl(
        override val name: String?,
        override val superType: LTClass? = null,
        override val interfaces: List<LTInterface> = emptyList(),
        override val memberFunctions: Map<String, LFunction> = emptyMap(),
        override val memberProperties: Map<String, LObjectProperty> = emptyMap()
    ) : LTObject {
        override fun hashCode(): Int {
            return name.hashCode() xor superType.hashCode() xor interfaces.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is LType) return false
            return name == other.name && superType == other.superType && interfaces == other.interfaces
        }
    }
}