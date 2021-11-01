package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class LoadFloatInsn(val valueConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.LOAD_FLOAT.ordinal).writeU24(valueConst)
    }
}


