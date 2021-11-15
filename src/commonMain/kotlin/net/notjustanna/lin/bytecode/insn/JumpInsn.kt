package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class JumpInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.JUMP.ordinal)
            .writeU24(labelCode.requireU24("JumpInsn#labelCode"))
    }
}
