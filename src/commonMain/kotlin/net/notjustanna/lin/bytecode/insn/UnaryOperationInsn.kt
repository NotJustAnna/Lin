package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.UnaryOperationType

data class UnaryOperationInsn(val operator: UnaryOperationType) : Insn()
