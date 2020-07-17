package xyz.avarel.lobos.tc

import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.complex.UnionType
import xyz.avarel.lobos.tc.generics.GenericBodyType
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.generics.GenericType

fun Collection<Type>.findGenericParameters(): List<GenericParameter> {
    val list = mutableListOf<GenericParameter>()
    for (type in this) {
        when (type) {
            is GenericType -> list.add(type.genericParameter)
            is TypeTemplate -> list.addAll(type.genericParameters)
        }
    }
    return list.distinct()
}

fun Type.transformToBodyType(): Type {
    return (this as? TypeTemplate)?.transformToBodyType() ?: this
}

fun TypeTemplate.transformToBodyType(): Type {
    return template(genericParameters.associateBy({ it }, { GenericBodyType(it) }))
}

fun List<Type>.toType(): Type {
    val list = flatMap(Type::toList).distinct().filter { it != NeverType }

    return when {
        list.isEmpty() -> NeverType
        list.size == 1 -> list[0]
        else -> UnionType(list)
    }
}

fun Type.toList(): List<Type> {
    return when {
        this is UnionType -> this.valueTypes
        else -> listOf(this)
    }
}