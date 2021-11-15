package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireI24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class PushDecimalInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_DECIMAL.ordinal)
            .writeU24(immediateValue.requireI24("PushDecimalInsn#immediateValue"))
    }
}
