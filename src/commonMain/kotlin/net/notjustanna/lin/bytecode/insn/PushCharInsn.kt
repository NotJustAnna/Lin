package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class PushCharInsn(val value: Char) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SIMPLE.ordinal)
            .writeByte(SimpleCode.PUSH_CHAR.ordinal)
            .writeShort(value.code)
    }
}
