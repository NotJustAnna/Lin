package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class NewFunctionInsn(val functionId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.NEW_FUNCTION.ordinal)
            .writeByte(0).writeShort(functionId) // TODO WRITE/READ U24
    }
}
