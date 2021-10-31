package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.BinaryOperationType

data class BinaryOperationInsn(val operator: BinaryOperationType) : Insn()
