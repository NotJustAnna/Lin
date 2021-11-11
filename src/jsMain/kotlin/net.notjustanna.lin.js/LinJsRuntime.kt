package net.notjustanna.lin.js

import net.notjustanna.lin.vm.LinRuntime
import net.notjustanna.lin.vm.scope.ImmutableMapScope
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LNativeFunction
import net.notjustanna.lin.vm.types.LNull

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
