package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class PushFloatInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_FLOAT.ordinal)
            .writeByte(0).writeShort(immediateValue) // TODO WRITE/READ U24
    }
}
