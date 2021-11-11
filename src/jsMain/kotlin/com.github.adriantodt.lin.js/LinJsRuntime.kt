package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.vm.LinRuntime
import com.github.adriantodt.lin.vm.scope.ImmutableMapScope
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LNativeFunction
import com.github.adriantodt.lin.vm.types.LNull

@OptIn(ExperimentalStdlibApi::class)
class LinJsRuntime {
    val scope: ImmutableMapScope
    val console = StringBuilder()

    init {
        val map = buildMap<String, LAny> {
            put("__ensureNotNull", LinRuntime.ensureNotNull)
            put("print", LNativeFunction { _, args ->
                console.append(args.joinToString(" "))
                LNull
            })
            put("println", LNativeFunction { _, args ->
                console.appendLine(args.joinToString(" "))
                LNull
            })
        }

        scope = ImmutableMapScope(map, null)
    }
}
