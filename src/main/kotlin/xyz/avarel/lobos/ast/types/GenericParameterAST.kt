package xyz.avarel.lobos.ast.types

class GenericParameterAST(
    val name: String,
    val parentType: TypeAST? = null
) {
    override fun toString() = buildString {
        append(name)
        if (parentType != null) {
            append(": ")
            append(parentType)
        }
    }
}