package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

public data class LoadDecimalInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_DECIMAL.ordinal)
            .writeU24(valueConst.requireU24("LoadDecimalInsn#valueConst"))
    }
}
