package net.notjustanna.lin.bytecode.insn

import okio.Buffer

object NewObjectInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeByte(0)
            .writeShort(ParameterlessCode.NEW_OBJECT.ordinal)
    }
}
