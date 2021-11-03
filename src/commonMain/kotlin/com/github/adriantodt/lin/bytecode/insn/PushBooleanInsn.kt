package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

data class PushBooleanInsn(val value: Boolean) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeU24((if (value) ParameterlessCode.PUSH_TRUE else ParameterlessCode.PUSH_FALSE).ordinal)
    }
}


