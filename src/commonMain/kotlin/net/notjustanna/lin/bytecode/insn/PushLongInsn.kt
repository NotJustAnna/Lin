package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class PushLongInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LONG.ordinal)
            .writeByte(0).writeShort(immediateValue) // TODO WRITE/READ U24
    }
}


