package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.BinaryOperationType

data class BinaryOperationInsn(val operator: BinaryOperationType) : Insn()
