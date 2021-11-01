package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class GetMemberPropertyInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.GET_MEMBER_PROPERTY.ordinal).writeU24(nameConst)
    }
}
