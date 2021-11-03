package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.writeU12Pair
import okio.Buffer

data class PushLoopHandlingInsn(val continueLabel: Int, val breakLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_LOOP_HANDLING.ordinal).writeU12Pair(continueLabel, breakLabel)
    }
}
