package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.vm.LAnyException
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LArray
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue

@OptIn(ExperimentalJsExport::class, ExperimentalTime::class)
@JsExport
class ExecutionResult internal constructor(timedRun: TimedValue<Result<LAny>>, log: StringBuilder) {
    val consoleLines = log.lines().toTypedArray()
    val runDuration = timedRun.duration.toString()
    val isError = timedRun.value.isFailure
    val result = timedRun.value.fold(LAny::toString) { e ->
        if (e is LAnyException) {
            val errorType = e.value.getMember("errorType")
            val description = e.value.getMember("description")
            val stackTrace = e.value.getMember("stackTrace") as? LArray
            if (errorType != null && description != null && stackTrace != null) {
                "Lin Error - $errorType: $description\n${stackTrace.value.joinToString("\n") { "  at $it" }}"
            } else {
                "Lin Object was thrown: ${e.value}"
            }
        } else "Platform Error: ${e.stackTraceToString()}"
    }
}
