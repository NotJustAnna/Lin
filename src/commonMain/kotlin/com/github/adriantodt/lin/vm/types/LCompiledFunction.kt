package com.github.adriantodt.lin.vm.types

import com.github.adriantodt.lin.bytecode.CompiledFunction
import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.vm.LinVirtualMachine
import com.github.adriantodt.lin.vm.impl.FunctionSetupLayer
import com.github.adriantodt.lin.vm.scope.Scope

class LCompiledFunction(
    val source: CompiledSource,
    val data: CompiledFunction,
    val rootScope: Scope
) : LFunction() {
    override val name: String?
        get() = source.stringConstOrNull(data.nameConst)

    override fun call(thisValue: LAny?, args: List<LAny>): LAny {
        return LinVirtualMachine { FunctionSetupLayer(it, this, thisValue, args.toList()) }.run().getOrThrow()
    }
}
