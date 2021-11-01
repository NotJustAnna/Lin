package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class CompiledParameter(val nameConst: Int, val varargs: Boolean, val defaultValueNodeId: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(nameConst).writeByte(if (varargs) 1 else 0).writeInt(defaultValueNodeId)
    }
}
