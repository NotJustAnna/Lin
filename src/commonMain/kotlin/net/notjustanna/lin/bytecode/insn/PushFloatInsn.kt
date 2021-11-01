package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class PushFloatInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_FLOAT.ordinal).writeU24(immediateValue)
    }
}
