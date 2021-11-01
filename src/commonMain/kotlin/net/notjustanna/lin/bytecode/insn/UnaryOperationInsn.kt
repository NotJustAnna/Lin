package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class UnaryOperationInsn(val operatorId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SIMPLE.ordinal)
            .writeByte(SimpleCode.UNARY_OPERATION.ordinal)
            .writeShort(operatorId)
    }
}
