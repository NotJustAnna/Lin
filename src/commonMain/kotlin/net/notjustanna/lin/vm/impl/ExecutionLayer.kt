package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.types.LAny

interface ExecutionLayer {
    fun step(): Boolean

    fun onReturn(value: LAny)

    fun onThrow(value: LAny)
}
