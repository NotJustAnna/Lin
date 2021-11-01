package net.notjustanna.lin.bytecode

import net.notjustanna.lin.bytecode.insn.Insn

data class CompiledNode(val instructions: List<Insn>, val labels: List<Label>)
