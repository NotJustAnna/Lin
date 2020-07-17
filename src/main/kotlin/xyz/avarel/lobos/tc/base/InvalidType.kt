package xyz.avarel.lobos.tc.base

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.scope.VariableInfo

/**
 * Signifies when type inference has failed.
 * Nothing can be assigned to it, ever.
 */
object InvalidType : AbstractType("[Invalid type.]") {
    override val parentType: Type = this
    override fun isAssignableFrom(other: Type) = false
    override fun getMember(key: String): VariableInfo? = null

    override fun union(other: Type) = this
    override fun intersect(other: Type) = this
}