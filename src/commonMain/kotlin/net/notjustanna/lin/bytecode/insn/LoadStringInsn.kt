package net.notjustanna.lin.bytecode.insn

import okio.Buffer

data class LoadStringInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_STRING.ordinal)
            .writeByte(0).writeShort(valueConst) // TODO WRITE/READ U24
    }
}
