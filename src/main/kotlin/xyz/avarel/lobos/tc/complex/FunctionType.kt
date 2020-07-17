package xyz.avarel.lobos.tc.complex

import xyz.avarel.lobos.tc.Type
import xyz.avarel.lobos.tc.TypeTemplate
import xyz.avarel.lobos.tc.base.NeverType
import xyz.avarel.lobos.tc.findGenericParameters
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.template

class FunctionType(
    val argumentTypes: List<Type>,
    val returnType: Type
) : TypeTemplate {
    override var genericParameters = (argumentTypes + returnType).findGenericParameters()

    override fun isAssignableFrom(other: Type): Boolean {
        return when {
            this === other -> true
            other === NeverType -> true
            other !is FunctionType -> false
            else -> argumentTypes.size == other.argumentTypes.size
                    && other.argumentTypes.zip(argumentTypes).all { (a, b) -> a.isAssignableFrom(b) }
                    && returnType.isAssignableFrom(other.returnType)
        }
    }

    override fun template(types: Map<GenericParameter, Type>): FunctionType {
        return FunctionType(argumentTypes.map { it.template(types) }, returnType.template(types))
    }

    override fun toString() = buildString {
        argumentTypes.joinTo(this, prefix = "(", postfix = ")")

        append(" -> ")
        append(returnType)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is FunctionType -> false
            argumentTypes != other.argumentTypes -> false
            returnType != other.returnType -> false
            genericParameters != other.genericParameters -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = argumentTypes.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + genericParameters.hashCode()
        return result
    }
}