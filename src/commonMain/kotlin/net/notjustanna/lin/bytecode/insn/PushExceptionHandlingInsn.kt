package net.notjustanna.lin.bytecode.insn

data class PushExceptionHandlingInsn(val catchLabel: Int, val endLabel: Int) : Insn()
