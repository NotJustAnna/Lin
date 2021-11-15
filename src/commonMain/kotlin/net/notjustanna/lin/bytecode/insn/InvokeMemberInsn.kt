package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU16
import net.notjustanna.lin.bytecode.utils.requireU8
import okio.Buffer

public data class InvokeMemberInsn(val nameConst: Int, val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE_MEMBER.ordinal)
            .writeShort(nameConst.requireU16("InvokeMemberInsn#nameConst"))
            .writeByte(size.requireU8("InvokeMemberInsn#size"))
    }
}
