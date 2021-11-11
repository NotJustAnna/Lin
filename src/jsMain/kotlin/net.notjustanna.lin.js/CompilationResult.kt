package net.notjustanna.lin.js

import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.LinVirtualMachine
import net.notjustanna.tartar.api.parser.SyntaxException
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@Suppress("unused")
@OptIn(ExperimentalJsExport::class, ExperimentalTime::class)
@JsExport
class CompilationResult internal constructor(timedResult: TimedValue<Result<CompiledSource>>) {
    val isError = timedResult.value.isFailure
    val isSyntaxError = timedResult.value.exceptionOrNull() is SyntaxException
    val errorMessage = timedResult.value.exceptionOrNull()?.message
    val errorStackTrace = timedResult.value.exceptionOrNull()?.stackTraceToString()
    private val compiledSource = timedResult.value.getOrNull()
    val compileDuration = timedResult.duration.toString()

    fun sourceToBytes(): ByteArray {
        if (compiledSource == null) {
            throw RuntimeException("Compilation failed.")
        }
        return compiledSource.toBytes().toByteArray()
    }

    fun sourceToHex(): String {
        if (compiledSource == null) {
            throw RuntimeException("Compilation failed.")
        }
        return compiledSource.toBytes().hex()
    }

    fun sourceToBase64(): String {
        if (compiledSource == null) {
            throw RuntimeException("Compilation failed.")
        }
        return compiledSource.toBytes().base64()
    }

    fun run(): ExecutionResult {
        if (compiledSource == null) {
            throw RuntimeException("Compilation failed.")
        }

        val runtime = LinJsRuntime()
        val vm = LinVirtualMachine(runtime.scope, compiledSource)
        return ExecutionResult(measureTimedValue { vm.runCatching { run() } }, runtime.console)
    }
}
