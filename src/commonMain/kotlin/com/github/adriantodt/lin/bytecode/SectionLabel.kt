package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class SectionLabel(val length: Int, val index: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(length).writeInt(index)
    }

    companion object : Deserializer<SectionLabel> {
        override fun deserializeFrom(buffer: Buffer): SectionLabel {
            return SectionLabel(buffer.readInt(), buffer.readInt())
        }
    }
}
