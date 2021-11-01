package com.github.adriantodt.lin.bytecode.insn

import okio.Buffer

data class BranchIfFalseInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.BRANCH_IF_FALSE.ordinal)
            .writeByte(0).writeShort(labelCode) // TODO WRITE/READ U24
    }
}
