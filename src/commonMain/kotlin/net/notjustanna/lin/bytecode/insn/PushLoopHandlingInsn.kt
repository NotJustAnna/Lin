package net.notjustanna.lin.bytecode.insn

data class PushLoopHandlingInsn(val breakLabel: Int, val continueLabel: Int) : Insn()
