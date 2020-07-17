package xyz.avarel.lobos.tc.complex

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.template

open class TupleType(val valueTypes: List<Type>) : TypeTemplate {
    override var genericParameters = valueTypes.findGenericParameters()

    override val isUnitType: Boolean get() = valueTypes.all(Type::isUnitType)

    override val universalType: Type by lazy { TupleType(valueTypes.map(Type::universalType)) }

    override fun template(types: Map<GenericParameter, Type>): Type {
        return TupleType(valueTypes.map { it.template(types) })
    }

    override fun isAssignableFrom(other: Type): Boolean {
        return when {
            this === other -> true
            other === NeverType -> true
            other is UnionType -> other.valueTypes.all { this isAssignableFrom it }
            other !is TupleType -> false
            else -> valueTypes.size == other.valueTypes.size
                    && valueTypes.zip(other.valueTypes).all { (a, b) -> a.isAssignableFrom(b) }
        }
    }

    override fun union(other: Type): Type {
        return when {
            other is TupleType && other.valueTypes.size == valueTypes.size -> TupleType(
                valueTypes.zip(
                    other.valueTypes,
                    Type::union
                )
            )
            else -> super.union(other)
        }
    }

    override fun intersect(other: Type): Type {
        return when {
            other is TupleType && other.valueTypes.size == valueTypes.size -> TupleType(
                valueTypes.zip(
                    other.valueTypes,
                    Type::intersect
                )
            )
            else -> super.intersect(other)
        }
    }

    override fun toString() = buildString {
        append('(')
        append(valueTypes[0])

        if (valueTypes.size == 1) {
            append(',')
        } else for (i in 1 until valueTypes.size) {
            append(", ")
            append(valueTypes[i])
        }

        append(')')
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is TupleType -> false
            valueTypes != other.valueTypes -> false
            genericParameters != other.genericParameters -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = valueTypes.hashCode()
        result = 31 * result + genericParameters.hashCode()
        return result
    }
}