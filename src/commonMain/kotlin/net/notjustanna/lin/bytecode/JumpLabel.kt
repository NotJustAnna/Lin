package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

public data class JumpLabel(val code: Int, val at: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(code).writeInt(at)
    }

    public companion object : Deserializer<JumpLabel> {
        override fun deserializeFrom(buffer: Buffer): JumpLabel {
            return JumpLabel(buffer.readInt(), buffer.readInt())
        }
    }
}
