package com.github.adriantodt.lin.bytecode

data class CompiledParameter(val name: String, val varargs: Boolean, val defaultValueNodeId: Int)
