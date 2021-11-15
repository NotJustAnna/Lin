package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

public data class NewFunctionInsn(val functionId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.NEW_FUNCTION.ordinal)
            .writeU24(functionId.requireU24("NewFunctionInsn#functionId"))
    }
}
