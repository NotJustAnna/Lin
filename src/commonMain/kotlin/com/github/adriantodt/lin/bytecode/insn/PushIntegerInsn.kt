package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireI24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

data class PushIntegerInsn(val immediateValue: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_INTEGER.ordinal)
            .writeU24(immediateValue.requireI24("PushIntegerInsn#immediateValue"))
    }
}


