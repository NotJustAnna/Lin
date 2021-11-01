package net.notjustanna.lin.compiler

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledParameter
import net.notjustanna.lin.bytecode.CompiledSource

class CompiledSourceBuilder {
    private val intPool = mutableListOf<Int>()
    private val longPool = mutableListOf<Long>()
    private val floatPool = mutableListOf<Float>()
    private val doublePool = mutableListOf<Double>()
    private val stringPool = mutableListOf<String>()

    private val functionParameters = mutableListOf<List<CompiledParameter>>()
    private val functions = mutableListOf<CompiledFunction>()

    private val nodeBuilders = mutableListOf<CompiledNodeBuilder>()

    fun newNodeBuilder(): CompiledNodeBuilder {
        val builder = CompiledNodeBuilder(this, nodeBuilders.size)
        nodeBuilders += builder
        return builder
    }

    fun constantId(value: String): Int {
        val indexOf = stringPool.indexOf(value)
        if (indexOf != -1) return indexOf
        stringPool.add(value)
        return stringPool.lastIndex
    }

    fun constantId(value: Double): Int {
        val indexOf = doublePool.indexOf(value)
        if (indexOf != -1) return indexOf
        doublePool.add(value)
        return stringPool.lastIndex
    }

    fun constantId(value: Float): Int {
        val indexOf = floatPool.indexOf(value)
        if (indexOf != -1) return indexOf
        floatPool.add(value)
        return stringPool.lastIndex
    }

    fun constantId(value: Int): Int {
        val indexOf = intPool.indexOf(value)
        if (indexOf != -1) return indexOf
        intPool.add(value)
        return stringPool.lastIndex
    }

    fun constantId(value: Long): Int {
        val indexOf = longPool.indexOf(value)
        if (indexOf != -1) return indexOf
        longPool.add(value)
        return stringPool.lastIndex
    }

    fun registerFunction(parametersId: Int, name: String?, bodyId: Int): Int {
        functions += CompiledFunction(parametersId, name, bodyId)
        return functions.lastIndex
    }

    fun registerParameters(parameters: List<CompiledParameter>): Int {
        functionParameters += parameters
        return functionParameters.lastIndex
    }

    fun build() = CompiledSource(
        intPool.toList(),
        longPool.toList(),
        floatPool.toList(),
        doublePool.toList(),
        stringPool.toList(),
        functionParameters.toList(),
        functions.toList(),
        nodeBuilders.map { it.build() }
    )
}
