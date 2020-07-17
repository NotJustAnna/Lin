package xyz.avarel.lobos.tc.base

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.scope.VariableInfo

/**
 * Represents the top of the inheritable type hierarchy.
 */
object AnyType : Type {
    override val parentType: Type = this
    override fun isAssignableFrom(other: Type) = other !== NullType && other !== InvalidType
    override fun getMember(key: String): VariableInfo? = null
    override fun toString() = "any"
}