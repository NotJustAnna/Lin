package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class NewFunctionInsn(val functionId: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.NEW_FUNCTION.ordinal)
            .writeU24(functionId.requireU24("NewFunctionInsn#functionId"))
    }
}
