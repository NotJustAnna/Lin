package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class GetMemberPropertyInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.GET_MEMBER_PROPERTY.ordinal)
            .writeByte(0).writeShort(nameConst) // TODO WRITE/READ U24
    }
}
