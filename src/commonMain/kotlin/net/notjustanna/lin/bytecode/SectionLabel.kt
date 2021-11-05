package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class SectionLabel(val length: Int, val index: Int) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(length).writeInt(index)
    }

    companion object : Deserializer<SectionLabel> {
        override fun deserializeFrom(buffer: Buffer): SectionLabel {
            return SectionLabel(buffer.readInt(), buffer.readInt())
        }
    }
}
