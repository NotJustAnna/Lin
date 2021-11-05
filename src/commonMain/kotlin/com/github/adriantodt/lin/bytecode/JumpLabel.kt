package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class JumpLabel(val code: Int, val at: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(code).writeInt(at)
    }

    companion object : Deserializer<JumpLabel> {
        override fun deserializeFrom(buffer: Buffer): JumpLabel {
            return JumpLabel(buffer.readInt(), buffer.readInt())
        }
    }
}
