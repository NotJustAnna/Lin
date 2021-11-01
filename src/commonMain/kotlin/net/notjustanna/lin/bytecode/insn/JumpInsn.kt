package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class JumpInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.JUMP.ordinal)
            .writeByte(0).writeShort(labelCode) // TODO WRITE/READ U24
    }
}
