package xyz.avarel.lobos.tc.base

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.scope.VariableInfo

/**
 * This type can be assigned to anything, because technically, it will never return.
 * However, no other type can be assigned to it.
 */
object NeverType : AbstractType("!") {
    override val parentType: Type = this
    override fun isAssignableFrom(other: Type) = other === this
    override fun getMember(key: String): VariableInfo? = null

    override fun union(other: Type) = other
    override fun intersect(other: Type) = this
}