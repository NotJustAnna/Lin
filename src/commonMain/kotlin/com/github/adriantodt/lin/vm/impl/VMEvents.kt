package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.vm.StackTrace
import com.github.adriantodt.lin.vm.types.LAny

public interface VMEvents {
    public fun pushLayer(layer: ExecutionLayer)

    public fun replaceLayer(layer: ExecutionLayer)

    public fun onReturn(value: LAny)

    public fun onThrow(value: LAny)

    public fun stackTrace(): List<StackTrace>
}
