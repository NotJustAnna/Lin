package com.github.adriantodt.lin.bytecode

data class CompiledSource(
    val intPool: List<Int>,
    val longPool: List<Long>,
    val floatPool: List<Float>,
    val doublePool: List<Double>,
    val stringPool: List<String>,
    val functionParameters: List<List<CompiledParameter>>,
    val functions: List<CompiledFunction>,
    val nodes: List<CompiledNode>
)
