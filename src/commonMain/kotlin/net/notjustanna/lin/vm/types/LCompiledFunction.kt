package net.notjustanna.lin.vm.types

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.LinVirtualMachine
import net.notjustanna.lin.vm.impl.FunctionSetupLayer
import net.notjustanna.lin.vm.scope.Scope

public class LCompiledFunction(
    public val source: CompiledSource,
    public val data: CompiledFunction,
    public val rootScope: Scope
) : LFunction() {
    override val name: String?
        get() = source.stringConstOrNull(data.nameConst)

    override fun call(thisValue: LAny?, args: List<LAny>): LAny {
        return LinVirtualMachine { FunctionSetupLayer(it, this, thisValue, args.toList()) }.run().getOrThrow()
    }
}
