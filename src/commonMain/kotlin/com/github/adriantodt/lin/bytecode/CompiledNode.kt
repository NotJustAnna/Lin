package com.github.adriantodt.lin.bytecode

import com.github.adriantodt.lin.bytecode.insn.Insn

data class CompiledNode(val instructions: List<Insn>, val labels: List<Label>)
