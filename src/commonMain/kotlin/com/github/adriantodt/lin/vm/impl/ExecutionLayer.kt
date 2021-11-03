package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.vm.types.LAny

interface ExecutionLayer {
    fun step()

    fun onReturn(value: LAny)

    fun onThrow(value: LAny)
}
