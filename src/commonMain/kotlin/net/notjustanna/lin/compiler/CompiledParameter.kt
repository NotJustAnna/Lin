package net.notjustanna.lin.compiler

data class CompiledParameter(
    val name: String,
    val varargs: Boolean,
    val defaultValueNodeId: Int
)
