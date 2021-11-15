package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

public data class GetMemberPropertyInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.GET_MEMBER_PROPERTY.ordinal)
            .writeU24(nameConst.requireU24("GetMemberPropertyInsn#nameConst"))
    }
}
