package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU16
import net.notjustanna.lin.bytecode.utils.requireU8
import okio.Buffer

data class InvokeLocalInsn(val nameConst: Int, val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE_LOCAL.ordinal)
            .writeShort(nameConst.requireU16("InvokeLocalInsn#nameConst"))
            .writeByte(size.requireU8("InvokeLocalInsn#size"))
    }
}
