package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class BinaryOperationInsn(val operatorId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SIMPLE.ordinal)
            .writeByte(SimpleCode.BINARY_OPERATION.ordinal)
            .writeShort(operatorId)
    }
}
