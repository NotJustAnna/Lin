package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.StackTrace
import net.notjustanna.lin.vm.types.LAny

public interface ExecutionLayer {
    public fun step()

    public fun onReturn(value: LAny)

    public fun onThrow(value: LAny)

    public fun trace(): StackTrace?
}
