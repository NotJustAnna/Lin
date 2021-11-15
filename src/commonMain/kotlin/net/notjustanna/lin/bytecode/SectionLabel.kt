package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

public data class SectionLabel(val length: Int, val index: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(length).writeInt(index)
    }

    public companion object : Deserializer<SectionLabel> {
        override fun deserializeFrom(buffer: Buffer): SectionLabel {
            return SectionLabel(buffer.readInt(), buffer.readInt())
        }
    }
}
