package com.github.adriantodt.lin.bytecode.insn

import com.github.adriantodt.lin.bytecode.utils.requireU16
import com.github.adriantodt.lin.bytecode.utils.requireU8
import okio.Buffer

public data class InvokeMemberInsn(val nameConst: Int, val size: Int) : Insn() {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeByte(Opcode.INVOKE_MEMBER.ordinal)
            .writeShort(nameConst.requireU16("InvokeMemberInsn#nameConst"))
            .writeByte(size.requireU8("InvokeMemberInsn#size"))
    }
}
