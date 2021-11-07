package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.vm.LinVirtualMachine
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
