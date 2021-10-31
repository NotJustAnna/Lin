package com.github.adriantodt.lin.bytecode.insn

sealed class Insn {
    override fun toString(): String {
        return this::class.simpleName ?: super.toString()
    }
}
