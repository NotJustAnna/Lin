package com.github.adriantodt.lin.bytecode.insn

data class DeclareVariableInsn(val name: String, val mutable: Boolean) : Insn()
