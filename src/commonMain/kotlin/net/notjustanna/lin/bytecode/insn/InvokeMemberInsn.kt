package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class InvokeMemberInsn(val nameConst: Int, val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE_MEMBER.ordinal)
            .writeShort(nameConst).writeByte(size)
    }
}
