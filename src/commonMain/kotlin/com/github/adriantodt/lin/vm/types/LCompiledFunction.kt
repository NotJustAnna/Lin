package com.github.adriantodt.lin.vm.types

import com.github.adriantodt.lin.bytecode.CompiledFunction
import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.vm.scope.Scope

class LCompiledFunction(
    val source: CompiledSource,
    val data: CompiledFunction,
    val rootScope: Scope
) : LFunction() {
    override val name: String?
        get() = source.stringConstOrNull(data.nameConst)

    override fun call(thisValue: LAny?, args: List<LAny>): LAny {
        if (data.bodyId == -1) {
            return LNull
        }
        TODO("Not yet implemented (Depends on Kotlin 1.6)")
    }
}
