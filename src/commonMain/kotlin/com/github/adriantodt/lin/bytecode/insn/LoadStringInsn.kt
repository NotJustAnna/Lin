package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

data class LoadStringInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_STRING.ordinal)
            .writeU24(valueConst.requireU24("LoadStringInsn#valueConst"))
    }
}
