package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.vm.LinRuntime
import com.github.adriantodt.lin.vm.scope.ImmutableMapScope
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LNativeFunction
import com.github.adriantodt.lin.vm.types.LNull

@OptIn(ExperimentalStdlibApi::class)
class LinJsRuntime {
    val scope: ImmutableMapScope
    val bufferedLog = mutableListOf<String>()

    init {
        val map = buildMap<String, LAny> {
            put("__ensureNotNull", LinRuntime.ensureNotNull)
            put("println", LNativeFunction { _, args ->
                bufferedLog.add(args.joinToString(" "))
                LNull
            })
        }

        scope = ImmutableMapScope(map, null)
    }
}
