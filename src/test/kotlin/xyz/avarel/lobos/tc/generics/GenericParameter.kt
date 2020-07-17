package xyz.avarel.lobos.tc.generics

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.base.AnyType

class GenericParameter(
    val name: String,
    val parentType: Type? = null
) {
    override fun toString() = buildString {
        append(name)
        if (parentType != null && parentType != AnyType) {
            append(": ")
            append(parentType)
        }
    }
}