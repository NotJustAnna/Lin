package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU12
import com.github.adriantodt.lin.bytecode.utils.writeU12Pair
import okio.Buffer

data class PushLoopHandlingInsn(val continueLabel: Int, val breakLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal)
            .writeU12Pair(
                continueLabel.requireU12("PushLoopHandlingInsn#continueLabel"),
                breakLabel.requireU12("PushLoopHandlingInsn#breakLabel")
            )
    }
}
