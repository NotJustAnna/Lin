package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

object BreakInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeByte(0)
            .writeShort(ParameterlessCode.BREAK.ordinal)
    }
}
