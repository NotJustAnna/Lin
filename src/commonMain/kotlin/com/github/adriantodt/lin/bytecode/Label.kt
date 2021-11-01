package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class Label(val code: Int, val at: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(code).writeInt(at)
    }

    companion object : Deserializer<Label> {
        override fun deserializeFrom(buffer: Buffer): Label {
            return Label(buffer.readInt(), buffer.readInt())
        }
    }
}
