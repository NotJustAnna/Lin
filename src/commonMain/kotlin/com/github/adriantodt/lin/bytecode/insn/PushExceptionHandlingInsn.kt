package com.github.adriantodt.lin.bytecode.insn

data class PushExceptionHandlingInsn(val catchLabel: Int, val endLabel: Int) : Insn()
