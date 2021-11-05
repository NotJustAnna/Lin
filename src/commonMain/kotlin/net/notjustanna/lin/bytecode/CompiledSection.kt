package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
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
