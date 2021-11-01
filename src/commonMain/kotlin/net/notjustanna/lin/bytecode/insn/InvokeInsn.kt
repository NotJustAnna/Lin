package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class InvokeInsn(val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE.ordinal)
            .writeShort(0).writeByte(size)
    }
}
