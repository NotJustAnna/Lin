package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class BranchIfTrueInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.BRANCH_IF_TRUE.ordinal)
            .writeByte(0).writeShort(labelCode) // TODO WRITE/READ U24
    }
}
