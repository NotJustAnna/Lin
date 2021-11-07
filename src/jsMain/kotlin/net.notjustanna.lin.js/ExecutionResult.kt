package net.notjustanna.lin.js

import net.notjustanna.lin.vm.LAnyException
import net.notjustanna.lin.vm.types.LAny
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue

@OptIn(ExperimentalJsExport::class, ExperimentalTime::class)
@JsExport
class ExecutionResult internal constructor(timedRun: TimedValue<Result<LAny>>) {
    val runDuration = timedRun.duration.toString()
    val isError = timedRun.value.isFailure
    val result = timedRun.value.fold(LAny::toString) {
        if (it is LAnyException) "Thrown: ${it.value}\n${it.stackTraceToString()}" else it.stackTraceToString()
    }
}
