package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class LoadIntInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_INT.ordinal)
            .writeByte(0).writeShort(valueConst) // TODO WRITE/READ U24
    }
}
