package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class LoadIntegerInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_INTEGER.ordinal)
            .writeU24(valueConst.requireU24("LoadIntegerInsn#valueConst"))
    }
}
