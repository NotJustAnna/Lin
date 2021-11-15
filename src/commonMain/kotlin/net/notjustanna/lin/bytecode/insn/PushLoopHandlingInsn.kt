package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU12
import net.notjustanna.lin.bytecode.utils.writeU12Pair
import okio.Buffer

public data class PushLoopHandlingInsn(val continueLabel: Int, val breakLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal)
            .writeU12Pair(
                continueLabel.requireU12("PushLoopHandlingInsn#continueLabel"),
                breakLabel.requireU12("PushLoopHandlingInsn#breakLabel")
            )
    }
}
