package net.notjustanna.lin.bytecode

import net.notjustanna.lin.bytecode.insn.Insn
import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer

data class CompiledNode(val instructions: List<Insn>, val labels: List<Label>) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(instructions.size)
        for (insn in instructions) insn.serializeTo(buffer)

        buffer.writeInt(labels.size)
        for (label in labels.sortedBy { it.code }) label.serializeTo(buffer)
    }

    companion object : Deserializer<CompiledNode> {
        override fun deserializeFrom(buffer: Buffer): CompiledNode {
            val instructions = mutableListOf<Insn>()
            repeat(buffer.readInt()) {
                instructions += Insn.deserializeFrom(buffer)
            }

            val labels = mutableListOf<Label>()
            repeat(buffer.readInt()) {
                labels += Label.deserializeFrom(buffer)
            }

            return CompiledNode(instructions, labels)
        }
    }
}
