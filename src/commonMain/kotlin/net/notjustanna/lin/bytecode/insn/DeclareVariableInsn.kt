package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class DeclareVariableInsn(val nameConst: Int, val mutable: Boolean) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte((if (mutable) Opcode.DECLARE_VARIABLE_MUTABLE else Opcode.DECLARE_VARIABLE_IMMUTABLE).ordinal)
            .writeByte(0).writeShort(nameConst) // TODO WRITE/READ U24
    }
}
