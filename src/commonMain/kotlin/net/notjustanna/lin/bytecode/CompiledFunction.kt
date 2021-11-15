package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class CompiledFunction(
    val parametersId: Int,
    val nameConst: Int,
    val bodyId: Int,
    val varargsParam: Int
) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(parametersId).writeInt(nameConst).writeInt(bodyId).writeByte(varargsParam)
    }

    companion object : Deserializer<CompiledFunction> {
        override fun deserializeFrom(buffer: Buffer): CompiledFunction {
            return CompiledFunction(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readByte().toInt() and 0xFF
            )
        }
    }
}
