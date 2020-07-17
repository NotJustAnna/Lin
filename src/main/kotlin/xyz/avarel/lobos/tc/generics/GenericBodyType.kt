package xyz.avarel.lobos.tc.generics

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.base.AnyType
import xyz.avarel.lobos.tc.base.NeverType

class GenericBodyType(val genericParameter: GenericParameter) :
    AbstractType(genericParameter.name, genericParameter.parentType ?: AnyType) {
    override fun isAssignableFrom(other: Type): Boolean {
        return other == this || other == NeverType || genericParameter.parentType?.isAssignableFrom(other) ?: false
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is GenericType -> genericParameter == other.genericParameter
            other is GenericBodyType -> genericParameter == other.genericParameter
            else -> false
        }
    }

    override fun toString() = genericParameter.name

    override fun hashCode(): Int {
        return genericParameter.hashCode()
    }
}