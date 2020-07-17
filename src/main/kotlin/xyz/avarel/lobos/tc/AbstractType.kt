package xyz.avarel.lobos.tc

import xyz.avarel.lobos.tc.base.AnyType
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.complex.UnionType
import xyz.avarel.lobos.tc.scope.VariableInfo

abstract class AbstractType(val name: String, override val parentType: Type = AnyType) : Type {
    override fun isAssignableFrom(other: Type): Boolean {
        return when (other) {
            this, NeverType -> true
            is UnionType -> other.valueTypes.all { this isAssignableFrom it }
            else -> {
                var currentType = other
                while (currentType != this) {
                    currentType = currentType.parentType
                    if (currentType.parentType == currentType) {
                        // Top-most types such as NullType, InvalidType, and AnyType
                        return false
                    }
                }
                true
            }
        }
    }

    override fun getMember(key: String): VariableInfo? = parentType.getMember(key)

    override fun toString() = name
}