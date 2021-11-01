package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

object PopLoopHandlingInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeByte(0)
            .writeShort(ParameterlessCode.POP_LOOP_HANDLING.ordinal)
    }
}
