package xyz.avarel.lobos.tc.struct

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.scope.VariableInfo
import xyz.avarel.lobos.tc.template

class TemplatedStructType(val base: BaseStructType, val typeArguments: Map<GenericParameter, Type>) : StructType {
    override val name get() = base.name
    override val members =
        base.members.mapValues { VariableInfo(it.value.type.template(typeArguments), it.value.mutable) }

    override var genericParameters = members.values.map(VariableInfo::type).findGenericParameters()

    override fun template(types: Map<GenericParameter, Type>): StructType {
        require(types.keys == genericParameters.toSet())
        return TemplatedStructType(base, typeArguments + types)
    }

    override fun isAssignableFrom(other: Type): Boolean {
        if (this == other) return true
        if (other !is TemplatedStructType) return false
        if (base !== other.base) return false

        return members.all { (key, value) ->
            other.members[key]?.let {
                value.type.isAssignableFrom(it.type)
            } ?: false
        }
    }

    override fun toString() = buildString {
        append(base.name)

        if (typeArguments.isNotEmpty()) {
            typeArguments.values.joinTo(this, prefix = "<", postfix = ">")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TemplatedStructType) return false

        if (base != other.base) return false
        if (typeArguments != other.typeArguments) return false
        if (members != other.members) return false
        if (genericParameters != other.genericParameters) return false

        return true
    }

    override fun hashCode(): Int {
        var result = base.hashCode()
        result = 31 * result + typeArguments.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + genericParameters.hashCode()
        return result
    }
}