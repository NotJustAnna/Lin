package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.writeU24
import okio.Buffer

data class LoadDecimalInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_DECIMAL.ordinal).writeU24(valueConst)
    }
}
