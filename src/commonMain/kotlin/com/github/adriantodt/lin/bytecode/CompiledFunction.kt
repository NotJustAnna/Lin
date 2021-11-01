package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class CompiledFunction(val parametersId: Int, val nameConst: Int, val bodyId: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(parametersId).writeInt(nameConst).writeInt(bodyId)
    }
}
