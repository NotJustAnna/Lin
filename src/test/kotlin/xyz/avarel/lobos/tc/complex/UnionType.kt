package xyz.avarel.lobos.tc.complex

import xyz.avarel.lobos.tc.*
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.scope.VariableInfo

class UnionType(val valueTypes: List<Type>) : TypeTemplate {
    override var genericParameters = valueTypes.findGenericParameters()

    init {
        require(valueTypes.size >= 2)
        require(valueTypes.firstOrNull { it is UnionType } == null)
    }

    override val universalType: Type by lazy {
        valueTypes.reduce(Type::commonSuperTypeWith)
    }

    override fun getMember(key: String): VariableInfo? {
        val members = valueTypes.map { it.getMember(key) }

        if (members.any { it == null }) return null

        val type = members.map { it!!.type }.reduce(Type::union)
        val mutable = members.all { it!!.mutable }
        return VariableInfo(type, mutable)
    }

    override fun template(types: Map<GenericParameter, Type>): Type {
        return valueTypes.map { it.template(types) }.toType()
    }

    override fun isAssignableFrom(other: Type): Boolean = when (other) {
        NeverType -> true
        is UnionType -> other.valueTypes.all { this isAssignableFrom it }
        else -> valueTypes.any { it isAssignableFrom other }
    }

    override fun toString() = valueTypes.joinToString(" | ")

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is UnionType -> false
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