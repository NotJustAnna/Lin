package xyz.avarel.lobos.tc.complex

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.template

class MapType(
    val keyType: Type,
    val valueType: Type
) : TypeTemplate {
    override var genericParameters = listOf(keyType, valueType).findGenericParameters()

    override fun isAssignableFrom(other: Type): Boolean {
        return when {
            this === other -> true
            other === NeverType -> true
            other !is MapType -> false
            other.keyType === NeverType && other.valueType === NeverType -> true
            else -> other.keyType isAssignableFrom keyType && valueType isAssignableFrom other.valueType
        }
    }

    override fun template(types: Map<GenericParameter, Type>): MapType {
        return MapType(keyType.template(types), valueType.template(types))
    }

    override fun toString() = "[$keyType: $valueType]"

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is MapType -> false
            keyType != other.keyType -> false
            valueType != other.valueType -> false
            genericParameters != other.genericParameters -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = keyType.hashCode()
        result = 31 * result + valueType.hashCode()
        result = 31 * result + genericParameters.hashCode()
        return result
    }
}