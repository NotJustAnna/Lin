package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.bytecode.insn.Insn
import com.github.adriantodt.lin.utils.Serializable
import okio.Buffer

data class CompiledNode(val instructions: List<Insn>, val labels: List<Label>) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(instructions.size)
        for (insn in instructions) insn.serializeTo(buffer)

        buffer.writeInt(labels.size)
        for (label in labels.sortedBy { it.code }) label.serializeTo(buffer)
    }
}
