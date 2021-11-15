package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.StackTrace
import net.notjustanna.lin.vm.types.LAny

public interface VMEvents {
    public fun pushLayer(layer: ExecutionLayer)

    public fun replaceLayer(layer: ExecutionLayer)

    public fun onReturn(value: LAny)

    public fun onThrow(value: LAny)

    public fun stackTrace(): List<StackTrace>
}
