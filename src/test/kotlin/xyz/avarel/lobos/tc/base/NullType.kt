package xyz.avarel.lobos.tc.base

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.scope.VariableInfo

/**
 * This represents the null type. Nothing can be assigned to it except for itself.
 */
object NullType : AbstractType("null") {
    override val isUnitType: Boolean get() = true
    override val parentType: Type = this
    override fun isAssignableFrom(other: Type) = other === this || other === NeverType
    override fun getMember(key: String): VariableInfo? = null
}