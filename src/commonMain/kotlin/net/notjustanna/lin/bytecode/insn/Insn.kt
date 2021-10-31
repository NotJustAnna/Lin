package net.notjustanna.lin.bytecode.insn

sealed class Insn {
    override fun toString(): String {
        return this::class.simpleName ?: super.toString()
    }
}
