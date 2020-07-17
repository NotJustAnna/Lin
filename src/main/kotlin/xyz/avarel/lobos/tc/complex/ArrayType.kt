package xyz.avarel.lobos.tc.complex

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.template

class ArrayType(
    val valueType: Type
) : TypeTemplate {
    override var genericParameters = listOf(valueType).findGenericParameters()

    override fun isAssignableFrom(other: Type): Boolean {
        return when {
            this === other -> true
            other === NeverType -> true
            other !is ArrayType -> false
            else -> valueType isAssignableFrom other.valueType
        }
    }

    override fun template(types: Map<GenericParameter, Type>): ArrayType {
        return ArrayType(valueType.template(types))
    }

    override fun toString() = "[$valueType]"

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is ArrayType -> false
            valueType != other.valueType -> false
            genericParameters != other.genericParameters -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = valueType.hashCode()
        result = 31 * result + genericParameters.hashCode()
        return result
    }
}