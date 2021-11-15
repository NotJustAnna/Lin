package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.bytecode.utils.readU8
import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

public data class CompiledFunction(
    val parametersId: Int,
    val nameConst: Int,
    val bodyId: Int,
    val varargsParam: Int
) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(parametersId)
            .writeInt(nameConst)
            .writeInt(bodyId)
            .writeByte(if (varargsParam == -1) 0 else 1)

        if (varargsParam != -1) {
            buffer.writeByte(varargsParam)
        }
    }

    public companion object : Deserializer<CompiledFunction> {
        override fun deserializeFrom(buffer: Buffer): CompiledFunction {
            return CompiledFunction(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                if (buffer.readU8() != 0) buffer.readU8() else -1
            )
        }
    }
}
