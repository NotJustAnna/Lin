package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class CompiledSection(val nameConst: Int, val line: Int, val column: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(nameConst).writeInt(line).writeInt(column)
    }

    companion object : Deserializer<CompiledSection> {
        override fun deserializeFrom(buffer: Buffer): CompiledSection {
            return CompiledSection(buffer.readInt(), buffer.readInt(), buffer.readInt())
        }
    }
}
