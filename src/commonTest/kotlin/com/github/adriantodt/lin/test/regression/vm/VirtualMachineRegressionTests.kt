package com.github.adriantodt.lin.test.regression.vm

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.lin.test.utils.ExecutionBenchmark
import com.github.adriantodt.lin.vm.types.*
import com.github.adriantodt.tartar.api.lexer.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VirtualMachineRegressionTests {
    @Test
    fun forLoop() {
        val code = """
            for (value in [1, 1L, 2f, 2.0, true, 'a', "abc"]) {
                publish(value)
            }
        """.trimIndent()

        Lin.lexer.parse(Source(code)) {
            println(it)
        }

        val execution = ExecutionBenchmark("forLoop", Source(code))

        assertEquals(LNull, execution.result, "Code should not produce result.")

        val array = listOf(
            LInteger(1), LInteger(1), LDecimal(2.0),
            LDecimal(2.0), LTrue, LString("a"), LString("abc")
        )

        for (any in array) {
            assertEquals(any, execution.output.removeFirst())
        }
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun variables() {
        val code = """
            val a = 1
            val b = 2
            publish(a, b)
        """.trimIndent()

        val execution = ExecutionBenchmark("variables", Source(code))

        assertEquals(LNull, execution.result, "Code should not produce result.")
        for (any in listOf(LInteger(1), LInteger(2))) {
            assertEquals(any, execution.output.removeFirst())
        }
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun simpleReturn() {
        val code = """
            "Hello, World!"
        """.trimIndent()

        val execution = ExecutionBenchmark("simpleReturn", Source(code))

        assertEquals(LString("Hello, World!"), execution.result, "Code should produce 'Hello, World!'")
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun returnBreakingFlow() {
        val code = """
            return false
            true
        """.trimIndent()

        val execution = ExecutionBenchmark("simpleReturn", Source(code))

        assertEquals(LFalse, execution.result, "Code should produce false")
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun breakWhileLoop() {
        val code = """
            var i = 100
            while (i) {
                break
                i = i - 1
            }
            i
        """.trimIndent()

        val execution = ExecutionBenchmark("breakWhileLoop", Source(code))

        assertEquals(LInteger(100), execution.result, "Code should produce 100")
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun continueWhileLoop() {
        val code = """
            var i = 100
            while (i) {
                i = i - 1
                continue
                return false
            }
            true
        """.trimIndent()

        val execution = ExecutionBenchmark("continueWhileLoop", Source(code))

        assertEquals(LTrue, execution.result, "Code should produce true")
        assertTrue(execution.output.isEmpty())
    }
}
