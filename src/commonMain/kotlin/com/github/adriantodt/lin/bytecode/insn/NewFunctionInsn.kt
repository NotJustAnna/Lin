package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

data class NewFunctionInsn(val functionId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.NEW_FUNCTION.ordinal).writeU24(functionId)
    }
}
