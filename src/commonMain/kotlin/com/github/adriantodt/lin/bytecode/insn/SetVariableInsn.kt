package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU24
import com.github.adriantodt.lin.bytecode.utils.writeU24
import okio.Buffer

public data class SetVariableInsn(val nameConst: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.SET_VARIABLE.ordinal)
            .writeU24(nameConst.requireU24("SetVariableInsn#nameConst"))
    }
}
