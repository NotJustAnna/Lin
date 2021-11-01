package com.github.adriantodt.lin.bytecode.insn

data class DeclareVariableInsn(val nameConst: Int, val mutable: Boolean) : Insn()
