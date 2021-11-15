package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

public data class PushCharInsn(val value: Char) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_CHAR.ordinal)
            .writeByte(0)
            .writeShort(value.code)
    }
}


