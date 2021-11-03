package com.github.adriantodt.lin.vm.types

import com.github.adriantodt.lin.bytecode.CompiledFunction
import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.vm.LinVirtualMachine
import com.github.adriantodt.lin.vm.impl.FunctionSetupLayer
import com.github.adriantodt.lin.vm.scope.Scope

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
