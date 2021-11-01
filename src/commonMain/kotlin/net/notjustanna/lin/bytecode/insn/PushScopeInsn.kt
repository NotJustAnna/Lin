package net.notjustanna.lin.bytecode.insn

import okio.Buffer

object PushScopeInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal)
            .writeByte(0)
            .writeShort(ParameterlessCode.PUSH_SCOPE.ordinal)
    }
}
