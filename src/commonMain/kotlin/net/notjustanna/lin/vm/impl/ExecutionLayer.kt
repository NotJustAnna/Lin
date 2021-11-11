package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.StackTrace
import net.notjustanna.lin.vm.types.LAny

interface ExecutionLayer {
    fun step()

    fun onReturn(value: LAny)

    fun onThrow(value: LAny)

    fun trace(): StackTrace?
}
