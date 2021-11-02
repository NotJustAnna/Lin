package net.notjustanna.lin.vm.types

import net.notjustanna.lin.bytecode.CompiledFunction
import net.notjustanna.lin.bytecode.CompiledSource
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

    class Native(val nativeBlock: (List<LAny>) -> LAny) : LFunction() {
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
            if (data.bodyId == -1) {
                return LNull
            }
            TODO("Not implemented yet.")
//            LinVM(source, source.nodes[data.bodyId])
        }

        override fun toString(): String {
            if (name != null) {
                return "<function $name>"
            }
            return "<function>"
        }
    }
}
