package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.vm.StackTrace
import com.github.adriantodt.lin.vm.types.LAny

public interface ExecutionLayer {
    public fun step()

    public fun onReturn(value: LAny)

    public fun onThrow(value: LAny)

    public fun trace(): StackTrace?
}
