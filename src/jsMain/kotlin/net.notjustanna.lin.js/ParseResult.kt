package net.notjustanna.lin.js

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.compiler.NodeCompiler
import net.notjustanna.tartar.api.parser.SyntaxException
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@Suppress("unused")
@OptIn(ExperimentalJsExport::class, ExperimentalTime::class)
@JsExport
class ParseResult internal constructor(timedResult: TimedValue<Result<Node>>) {
    val isError = timedResult.value.isFailure
    val isSyntaxError = timedResult.value.exceptionOrNull() is SyntaxException
    val errorMessage = timedResult.value.exceptionOrNull()?.message
    val errorStackTrace = timedResult.value.exceptionOrNull()?.stackTraceToString()
    private val parsedNode = timedResult.value.getOrNull()
    val parseDuration = timedResult.duration.toString()

    fun compile(): CompilationResult {
        if (parsedNode == null) {
            throw RuntimeException("Parsing failed.")
        }
        return CompilationResult(measureTimedValue { NodeCompiler.runCatching { compile(parsedNode) } })
    }
}
