package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class SetVariableInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SET_VARIABLE.ordinal)
            .writeByte(0).writeShort(nameConst) // TODO WRITE/READ U24
    }
}
