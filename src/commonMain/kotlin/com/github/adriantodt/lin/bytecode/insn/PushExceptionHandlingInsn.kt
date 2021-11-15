package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU12
import com.github.adriantodt.lin.bytecode.utils.writeU12Pair
import okio.Buffer

data class PushExceptionHandlingInsn(val catchLabel: Int, val endLabel: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PUSH_EXCEPTION_HANDLING.ordinal)
            .writeU12Pair(
                catchLabel.requireU12("PushExceptionHandlingInsn#catchLabel"),
                endLabel.requireU12("PushExceptionHandlingInsn#endLabel")
            )
    }
}
