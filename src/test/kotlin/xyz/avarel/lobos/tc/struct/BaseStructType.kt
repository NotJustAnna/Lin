package xyz.avarel.lobos.tc.struct

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.base.AnyType
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.scope.VariableInfo

class BaseStructType(
    override val name: String,
    val parentStructType: StructType?,
    override val members: Map<String, VariableInfo>
) : StructType {
    override val parentType: Type get() = parentStructType ?: AnyType

    override var genericParameters = members.values.map(VariableInfo::type).findGenericParameters()

    override fun template(types: Map<GenericParameter, Type>): StructType {
        require(types.keys == genericParameters.toSet())
        return TemplatedStructType(this, types)
    }

    override fun isAssignableFrom(other: Type): Boolean {
        return this === other
    }

    override fun toString() = buildString {
        append(name)

        if (genericParameters.isNotEmpty()) {
            genericParameters.joinTo(this, prefix = "<", postfix = ">")
        }
    }
}

