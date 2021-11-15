package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class SetMemberPropertyInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SET_MEMBER_PROPERTY.ordinal)
            .writeU24(nameConst.requireU24("SetMemberPropertyInsn#nameConst"))
    }
}
