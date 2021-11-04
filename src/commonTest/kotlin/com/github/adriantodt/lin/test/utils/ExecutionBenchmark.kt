package com.github.adriantodt.lin.test.utils

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.lin.compiler.NodeCompiler
import com.github.adriantodt.lin.vm.LinVirtualMachine
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.unifiedplatform.currentPlatform
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
class ExecutionBenchmark(private val name: String, source: Source, inputValues: List<LAny> = emptyList()) {
    val result: LAny
    val input: MutableList<LAny>
    val output: MutableList<LAny>

    private val parseDuration: Duration
    private val compileDuration: Duration
    private val executionDuration: Duration

    init {
        val (node, parseDuration) = measureTimedValue { Lin.parser.parse(source) }
        val (compiledSource, compileDuration) = measureTimedValue { NodeCompiler.compile(node) }
        val (input, output, scope) = TestScope(inputValues)
        val (result, executionDuration) = measureTimedValue { LinVirtualMachine(compiledSource, scope).run() }

        this.result = result
        this.input = input
        this.output = output

        this.parseDuration = parseDuration
        this.compileDuration = compileDuration
        this.executionDuration = executionDuration

        reportBenchmark()
    }

    private fun reportBenchmark() {
        val title = """
            Benchmark -- $name $currentPlatform
        """.trimIndent()
        val report = """
            Parsing:    $parseDuration
            Compiling:  $compileDuration
            Executing:  $executionDuration
            Total:      ${parseDuration + compileDuration + executionDuration}
        """.trimIndent()

        val n = listOf(title, report).flatMap { it.lines() }.maxOf { it.length }

        val separator = "+" + "-".repeat(n + 2) + "+"

        val str = listOf(title, report).joinToString(separator, separator, separator + "\n") { s ->
            "\n" + s.lines().joinToString("\n") { "| " + it.padEnd(n) + " |" } + "\n"
        }

        println(str)
    }
}
