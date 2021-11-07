package net.notjustanna.lin.js

import net.notjustanna.lin.vm.LinRuntime
import net.notjustanna.lin.vm.scope.ImmutableMapScope
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LNativeFunction
import net.notjustanna.lin.vm.types.LNull

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
