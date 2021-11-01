package net.notjustanna.lin.compiler

class CompiledSourceBuilder {
    private val nodeBuilders = mutableListOf<CompiledNodeBuilder>()

    fun newNodeBuilder(): CompiledNodeBuilder {
        val builder = CompiledNodeBuilder(nodeBuilders.size)
        nodeBuilders += builder
        return builder
    }
}
