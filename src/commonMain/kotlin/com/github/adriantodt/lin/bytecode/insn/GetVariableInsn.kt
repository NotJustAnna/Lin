package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class GetVariableInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.GET_VARIABLE.ordinal)
            .writeByte(0).writeShort(nameConst) // TODO WRITE/READ U24
    }
}
