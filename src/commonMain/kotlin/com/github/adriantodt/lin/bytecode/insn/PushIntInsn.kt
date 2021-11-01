package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class PushIntInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_INT.ordinal)
            .writeByte(0).writeShort(immediateValue) // TODO WRITE/READ U24
    }
}


