package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

data class JumpInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.JUMP.ordinal).writeU24(labelCode)
    }
}
