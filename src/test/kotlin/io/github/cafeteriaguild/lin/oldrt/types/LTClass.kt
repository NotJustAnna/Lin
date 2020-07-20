package io.github.cafeteriaguild.lin.oldrt.types

import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction
import io.github.cafeteriaguild.lin.oldrt.types.properties.LObjectProperty


/**
 * Represents a Lin class
 */
interface LTClass : LType {
    val constructors: List<LFunction>

    abstract class Base : LTClass {
        override val name: String? = null
        override val superType: LTClass? = LTAny
        override val interfaces: List<LTInterface> = emptyList()
        override val memberFunctions: Map<String, LFunction> = emptyMap()
        override val memberProperties: Map<String, LObjectProperty> = emptyMap()
        override val constructors: List<LFunction> = emptyList()

        override fun hashCode(): Int {
            return name.hashCode() xor superType.hashCode() xor interfaces.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is LType) return false
            return name == other.name && superType == other.superType && interfaces == other.interfaces
        }
    }

    data class Impl(
        override val name: String? = null,
        override val superType: LTClass? = LTAny,
        override val interfaces: List<LTInterface> = emptyList(),
        override val memberFunctions: Map<String, LFunction> = emptyMap(),
        override val memberProperties: Map<String, LObjectProperty> = emptyMap(),
        override val constructors: List<LFunction> = emptyList()
    ) : LTClass {
        override fun hashCode(): Int {
            return name.hashCode() xor superType.hashCode() xor interfaces.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is LType) return false
            return name == other.name && superType == other.superType && interfaces == other.interfaces
        }
    }
}