package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

public data class JumpInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.JUMP.ordinal)
            .writeU24(labelCode.requireU24("JumpInsn#labelCode"))
    }
}
