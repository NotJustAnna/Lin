package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class LoadDoubleInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_DOUBLE.ordinal).writeU24(valueConst)
    }
}
