package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

public data class CompiledSection(val nameConst: Int, val line: Int, val column: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(nameConst).writeInt(line).writeInt(column)
    }

    public companion object : Deserializer<CompiledSection> {
        override fun deserializeFrom(buffer: Buffer): CompiledSection {
            return CompiledSection(buffer.readInt(), buffer.readInt(), buffer.readInt())
        }
    }
}
