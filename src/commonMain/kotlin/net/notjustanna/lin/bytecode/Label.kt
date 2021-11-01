package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class Label(val code: Int, val at: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(code).writeInt(at)
    }
}
