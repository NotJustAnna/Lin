package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class PushExceptionHandlingInsn(val catchLabel: Int, val endLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_EXCEPTION_HANDLING.ordinal)
            .writeByte(0).writeByte(catchLabel).writeByte(endLabel) // TODO WRITE/READ U12
    }
}
