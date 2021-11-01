package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.writeU24
import okio.Buffer

data class PushDecimalInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_DECIMAL.ordinal).writeU24(immediateValue)
    }
}
