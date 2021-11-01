package net.notjustanna.lin.bytecode

data class CompiledParameter(val name: String, val varargs: Boolean, val defaultValueNodeId: Int)
