package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireI24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class PushIntegerInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_INTEGER.ordinal)
            .writeU24(immediateValue.requireI24("PushIntegerInsn#immediateValue"))
    }
}


