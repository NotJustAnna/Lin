package xyz.avarel.lobos.tc.struct

import xyz.avarel.lobos.tc.AbstractType
import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.generics.GenericParameter

class StructConstructorType(val structType: StructType) : AbstractType("$structType constructor"), TypeTemplate {
    override var genericParameters: List<GenericParameter>
        get() = structType.genericParameters
        set(_) = throw IllegalStateException()

    override fun template(types: Map<GenericParameter, Type>): Type {
        return StructConstructorType(structType.template(types))
    }
}