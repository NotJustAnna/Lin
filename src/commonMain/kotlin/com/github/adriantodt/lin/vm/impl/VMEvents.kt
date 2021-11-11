package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.vm.StackTrace
import com.github.adriantodt.lin.vm.types.LAny

interface VMEvents {
    fun pushLayer(layer: ExecutionLayer)

    fun replaceLayer(layer: ExecutionLayer)

    fun onReturn(value: LAny)

    fun onThrow(value: LAny)

    fun stackTrace(): List<StackTrace>
}
