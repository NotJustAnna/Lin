package net.notjustanna.lin.vm.types

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.scope.Scope

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
