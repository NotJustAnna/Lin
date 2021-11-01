package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Deserializer
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class CompiledParameter(val nameConst: Int, val varargs: Boolean, val defaultValueNodeId: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(nameConst).writeByte(if (varargs) 1 else 0).writeInt(defaultValueNodeId)
    }

    companion object : Deserializer<CompiledParameter> {
        override fun deserializeFrom(buffer: Buffer): CompiledParameter {
            return CompiledParameter(buffer.readInt(), buffer.readByte().toInt() != 0, buffer.readInt())
        }
    }
}
