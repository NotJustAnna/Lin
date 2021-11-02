package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.utils.writeU24
import okio.Buffer

object BinaryLteOperationInsn : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.PARAMETERLESS.ordinal).writeU24(ParameterlessCode.BINARY_LTE.ordinal)
    }
}
