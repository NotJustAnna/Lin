package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU12Pair
import okio.Buffer

data class PushLoopHandlingInsn(val breakLabel: Int, val continueLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal).writeU12Pair(breakLabel, continueLabel)
    }
}
