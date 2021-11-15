package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class CompiledParameter(val nameConst: Int, val defaultValueNodeId: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(nameConst).writeInt(defaultValueNodeId)
    }

    companion object : Deserializer<CompiledParameter> {
        override fun deserializeFrom(buffer: Buffer): CompiledParameter {
            return CompiledParameter(buffer.readInt(), buffer.readInt())
        }
    }
}
