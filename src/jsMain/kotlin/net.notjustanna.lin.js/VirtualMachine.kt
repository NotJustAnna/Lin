package net.notjustanna.lin.js

import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.LinVirtualMachine
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalJsExport::class, ExperimentalTime::class)
@JsExport
class VirtualMachine internal constructor(private val source: CompiledSource) {
    private val vm = LinVirtualMachine(LinJsRuntime.scope, source)

    fun run(): ExecutionResult {
        return ExecutionResult(measureTimedValue { vm.runCatching { run() } })
    }
}
