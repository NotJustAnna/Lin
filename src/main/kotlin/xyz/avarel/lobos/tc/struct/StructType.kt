package xyz.avarel.lobos.tc.struct

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.scope.VariableInfo

interface StructType : TypeTemplate {
    val name: String
    val members: Map<String, VariableInfo>
    override fun getMember(key: String): VariableInfo? = members[key]
    override fun template(types: Map<GenericParameter, Type>): StructType
}