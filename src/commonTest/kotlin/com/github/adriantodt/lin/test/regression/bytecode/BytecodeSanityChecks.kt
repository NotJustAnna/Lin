package com.github.adriantodt.lin.test.regression.bytecode

import com.github.adriantodt.lin.bytecode.insn.Insn
import com.github.adriantodt.lin.bytecode.utils.maxU24
import kotlin.math.floor
import kotlin.test.Test
import kotlin.test.assertTrue

class BytecodeSanityChecks {
    @Test
    fun insnOpcodeEnum() {
        val count = Insn.Opcode.values().size
        val max = 0xFF

        val report = listOf(
            "Opcodes [U8]",
            "Max: $max opcodes, Defined: $count opcodes (${floor(count.toDouble() / max * 10000) / 100}%)",
            if (count <= max) "Available: ${max - count} opcodes" else "Exceeding: ${count - max} opcodes"
        )
        println(report.joinToString("\n", "", "\n"))
        assertTrue(count <= max)
    }

    @Test
    fun insnParameterlessEnum() {
        val count = Insn.ParameterlessCode.values().size
        val max = maxU24

        val report = listOf(
            "Parameterless Codes [U24]",
            "Max: $max codes, Defined: $count codes (${floor(count.toDouble() / max * 10000) / 100}%)",
            if (count <= max) "Available: ${max - count} codes" else "Exceeding: ${count - max} codes"
        )
        println(report.joinToString("\n", "", "\n"))
        assertTrue(count <= max)
    }
}
