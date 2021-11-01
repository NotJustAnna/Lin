package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class SetMemberPropertyInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SET_MEMBER_PROPERTY.ordinal)
            .writeByte(0).writeShort(nameConst) // TODO WRITE/READ U24
    }
}
