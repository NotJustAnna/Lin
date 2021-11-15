package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

public data class BranchIfInsn(val value: Boolean, val labelCode: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte((if (value) Opcode.BRANCH_IF_TRUE else Opcode.BRANCH_IF_FALSE).ordinal)
            .writeU24(labelCode.requireU24("BranchIfInsn#labelCode"))
    }
}
