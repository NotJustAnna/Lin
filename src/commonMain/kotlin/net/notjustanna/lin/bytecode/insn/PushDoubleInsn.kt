package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class PushDoubleInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_DOUBLE.ordinal)
            .writeByte(0).writeShort(immediateValue) // TODO WRITE/READ U24
    }
}
