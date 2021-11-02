package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.types.LAny

interface VMEvents {
    fun pushLayer(layer: ExecutionLayer)

    fun replaceLayer(layer: ExecutionLayer)

    fun onReturn(value: LAny)

    fun onThrow(value: LAny)
}
