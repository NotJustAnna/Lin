package com.github.adriantodt.lin.bytecode.insn

data class PushLoopHandlingInsn(val breakLabel: Int, val continueLabel: Int) : Insn()
