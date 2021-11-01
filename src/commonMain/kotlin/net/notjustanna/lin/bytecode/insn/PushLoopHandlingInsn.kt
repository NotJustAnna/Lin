package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class PushLoopHandlingInsn(val breakLabel: Int, val continueLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal)
            .writeByte(0).writeByte(breakLabel).writeByte(continueLabel) // TODO WRITE/READ U12
    }
}
