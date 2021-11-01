package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class LoadLongInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_LONG.ordinal)
            .writeByte(0).writeShort(valueConst) // TODO WRITE/READ U24
    }
}
