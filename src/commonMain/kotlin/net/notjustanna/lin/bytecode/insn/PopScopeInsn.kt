package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

object PopScopeInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal).writeU24(ParameterlessCode.POP_SCOPE.ordinal)
    }
}
