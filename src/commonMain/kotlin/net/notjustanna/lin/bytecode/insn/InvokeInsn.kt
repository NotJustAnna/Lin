package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU8
import okio.Buffer

public data class InvokeInsn(val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE.ordinal)
            .writeShort(0)
            .writeByte(size.requireU8("InvokeInsn#size"))
    }
}
