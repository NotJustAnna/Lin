package net.notjustanna.lin.vm.types

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.LinVirtualMachine
import net.notjustanna.lin.vm.impl.FunctionSetupLayer
import net.notjustanna.lin.vm.scope.Scope

sealed class LFunction : LAny() {
    override fun truth(): Boolean {
        return true
    }

    override val linType: String
        get() = "function"

    abstract operator fun invoke(vararg arguments: LAny): LAny

    override fun getMember(name: String): LAny? {
        return null
    }

    class Native(val nativeBlock: (params: List<LAny>) -> LAny) : LFunction() {
        override fun invoke(vararg arguments: LAny): LAny {
            return nativeBlock(arguments.toList())
        }

        override fun toString(): String {
            return "<native function>"
        }
    }

    class Compiled(
        val name: String?,
        val source: CompiledSource,
        val data: CompiledFunction,
        val rootScope: Scope
    ) : LFunction() {
        override fun invoke(vararg arguments: LAny): LAny {
            return LinVirtualMachine { FunctionSetupLayer(it, this, null, arguments.toList()) }.run()
        }

        override fun toString(): String {
            if (name != null) {
                return "<function $name>"
            }
            return "<function>"
        }
    }
}
