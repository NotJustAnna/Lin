package net.notjustanna.lin.bytecode.insn

import net.notjustanna.lin.utils.writeU24
import okio.Buffer

data class BranchIfTrueInsn(val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.BRANCH_IF_TRUE.ordinal).writeU24(labelCode)
    }
}
