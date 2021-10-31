package net.notjustanna.lin.bytecode.insn

data class DeclareVariableInsn(val name: String, val mutable: Boolean) : Insn()
