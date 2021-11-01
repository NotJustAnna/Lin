package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.writeU12Pair
import okio.Buffer

data class PushLoopHandlingInsn(val breakLabel: Int, val continueLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal).writeU12Pair(breakLabel, continueLabel)
    }
}
