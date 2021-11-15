package net.notjustanna.lin.compiler

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledParameter
import net.notjustanna.lin.bytecode.CompiledSection
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.tartar.api.lexer.Section

public class CompiledSourceBuilder {
    private val longPool = mutableListOf<Long>()
    private val stringPool = mutableListOf<String>()

    private val sections = mutableListOf<CompiledSection>()

    private val functionParameters = mutableListOf<List<CompiledParameter>>()
    private val functions = mutableListOf<CompiledFunction>()

    private val nodeBuilders = mutableListOf<CompiledNodeBuilder>()

    public fun newNodeBuilder(): CompiledNodeBuilder {
        val builder = CompiledNodeBuilder(this, nodeBuilders.size)
        nodeBuilders += builder
        return builder
    }

    public fun sectionId(section: Section): Int {
        val source = section.source
        val nameConst = constantId(source.name)
        val value = CompiledSection(
            nameConst,
            section.firstLine.lineNumber,
            section.index - section.firstLine.range.first
        )
        val indexOf = sections.indexOf(value)
        if (indexOf != -1) return indexOf
        sections.add(value)
        return sections.lastIndex
    }

    public fun constantId(value: String): Int {
        val indexOf = stringPool.indexOf(value)
        if (indexOf != -1) return indexOf
        stringPool.add(value)
        return stringPool.lastIndex
    }

    public fun constantId(value: Double): Int {
        return constantId(value.toBits())
    }

    public fun constantId(value: Long): Int {
        val indexOf = longPool.indexOf(value)
        if (indexOf != -1) return indexOf
        longPool.add(value)
        return stringPool.lastIndex
    }

    public fun registerFunction(parametersId: Int, name: String?, bodyId: Int, varargsParam: Int): Int {
        functions += CompiledFunction(parametersId, name?.let(this::constantId) ?: -1, bodyId, varargsParam)
        return functions.lastIndex
    }

    public fun registerParameters(parameters: List<CompiledParameter>): Int {
        functionParameters += parameters
        return functionParameters.lastIndex
    }

    public fun build(): CompiledSource {
        return CompiledSource(
            longPool.toList(),
            stringPool.toList(),
            functionParameters.toList(),
            functions.toList(),
            sections.toList(),
            nodeBuilders.map { it.build() }
        )
    }
}
