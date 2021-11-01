package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class SetSubscriptInsn(val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SET_SUBSCRIPT.ordinal)
            .writeShort(0).writeByte(size)
    }
}
