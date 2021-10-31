package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.UnaryOperationType

data class UnaryOperationInsn(val operator: UnaryOperationType) : Insn()
