package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU12
import net.notjustanna.lin.bytecode.utils.writeU12Pair
import okio.Buffer

public data class PushExceptionHandlingInsn(val catchLabel: Int, val endLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_EXCEPTION_HANDLING.ordinal)
            .writeU12Pair(
                catchLabel.requireU12("PushExceptionHandlingInsn#catchLabel"),
                endLabel.requireU12("PushExceptionHandlingInsn#endLabel")
            )
    }
}
