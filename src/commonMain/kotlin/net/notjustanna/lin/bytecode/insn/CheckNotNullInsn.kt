package net.notjustanna.lin.bytecode.insn

import okio.Buffer

object CheckNotNullInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeByte(0)
            .writeShort(ParameterlessCode.CHECK_NOT_NULL.ordinal)
    }
}
