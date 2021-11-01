package net.notjustanna.lin.bytecode.insn

data class DeclareVariableInsn(val nameConst: Int, val mutable: Boolean) : Insn()
