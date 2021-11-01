package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class PushBooleanInsn(val value: Boolean) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SIMPLE.ordinal)
            .writeByte(SimpleCode.PUSH_BOOLEAN.ordinal)
            .writeShort(if (value) 1 else 0)
    }
}


