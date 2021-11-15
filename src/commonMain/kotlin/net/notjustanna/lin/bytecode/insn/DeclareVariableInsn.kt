package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.bytecode.utils.requireU24
import net.notjustanna.lin.bytecode.utils.writeU24
import okio.Buffer

data class DeclareVariableInsn(val nameConst: Int, val mutable: Boolean) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte((if (mutable) Opcode.DECLARE_VARIABLE_MUTABLE else Opcode.DECLARE_VARIABLE_IMMUTABLE).ordinal)
            .writeU24(nameConst.requireU24("DeclareVariableInsn#nameConst"))
    }
}
