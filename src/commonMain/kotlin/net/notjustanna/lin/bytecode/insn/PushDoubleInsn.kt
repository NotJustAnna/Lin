package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class PushDoubleInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_DOUBLE.ordinal).writeU24(immediateValue)
    }
}
