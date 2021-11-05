package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class CompiledSection(val pathConst: Int, val nameConst: Int, val index: Int, val length: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(pathConst).writeInt(nameConst).writeInt(index).writeInt(length)
    }


    companion object : Deserializer<CompiledSection> {
        override fun deserializeFrom(buffer: Buffer): CompiledSection {
            return CompiledSection(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt())
        }
    }
}
