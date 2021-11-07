package com.github.adriantodt.lin.test.regression.vm

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
    fun forLoopWithRange() {
        val code = """
            for (value in 0..100) {
                publish(value)
            }
        """.trimIndent()

        val execution = ExecutionBenchmark("forLoopWithRange", Source(code))

        assertEquals(LNull, execution.result, "Code should not produce result.")

        for (i in 0L..100L) {
            assertEquals(LInteger(i), execution.output.removeFirst(), "Error at index $i")
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
        for ((index, any) in listOf(LInteger(1), LInteger(2)).withIndex()) {
            assertEquals(any, execution.output.removeFirst(), "Error at index $index")
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

    @Test
    fun compiledFunctionInvocation() {
        val code = """
            (fun() {
                return true
            })()
        """.trimIndent()

        val execution = ExecutionBenchmark("compiledFunctionInvocation", Source(code))

        assertEquals(LTrue, execution.result, "Code should produce true")
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun compiledFunctionInvocationWithDefaultValue() {
        val code = """
            (fun(name = "World!") {
                return "Hello, " + name
            })()
        """.trimIndent()

        val execution = ExecutionBenchmark("compiledFunctionInvocationWithDefaultValue", Source(code))

        assertEquals(LString("Hello, World!"), execution.result, "Code should produce 'Hello, World!'")
        assertTrue(execution.output.isEmpty())
    }

    @Test
    fun numberUnaryAndBinaryOperators() {
        val code = """
            val i1 = 1
            val i2 = 2
            val d1 = 1.0
            val d2 = 2.0
            publish(
                -i1, -d1, +i1, +d1,
                i1 + i2, i1 + d2, d1 + i2, d1 + d2,
                i1 - i2, i1 - d2, d1 - i2, d1 - d2,
                i1 * i2, i1 * d2, d1 * i2, d1 * d2,
                i1 / i2, i1 / d2, d1 / i2, d1 / d2,
                i1 % i2, i1 % d2, d1 % i2, d1 % d2
            )
        """.trimIndent()

        val execution = ExecutionBenchmark("numberUnaryAndBinaryOperators", Source(code))

        assertEquals(LNull, execution.result, "Code should not produce result.")

        val intNegOne = LInteger(-1)
        val decNegOne = LDecimal(-1.0)
        val intZero = LInteger(0)
        val decHalf = LDecimal(0.5)
        val intOne = LInteger(1)
        val decOne = LDecimal(1.0)
        val intTwo = LInteger(2)
        val decTwo = LDecimal(2.0)
        val intThree = LInteger(3)
        val decThree = LDecimal(3.0)

        val array = listOf(
            intNegOne, decNegOne, intOne, decOne,
            intThree, decThree, decThree, decThree,
            intNegOne, decNegOne, decNegOne, decNegOne,
            intTwo, decTwo, decTwo, decTwo,
            intZero, decHalf, decHalf, decHalf,
            intOne, decOne, decOne, decOne,
        )

        for ((index, any) in array.withIndex()) {
            assertEquals(any, execution.output.removeFirst(), "Error at index $index")
        }
        assertTrue(execution.output.isEmpty(), "Output array: ${execution.output}")
    }

    @Test
    fun testInOperator() {
        val code = """
            val a = ["foo"]
            val b = {"foo":true}
            publish("foo" in a, "bar" in a, "foo" in b, "bar" in b)
        """.trimIndent()

        val execution = ExecutionBenchmark("testInOperator", Source(code))

        assertEquals(LNull, execution.result, "Code should not produce result.")

        for ((index, any) in listOf(LTrue, LFalse, LTrue, LFalse).withIndex()) {
            assertEquals(any, execution.output.removeFirst(), "Error at index $index")
        }
        assertTrue(execution.output.isEmpty(), "Output array: ${execution.output}")
    }

    @Test
    fun tryCatch() {
        val code = """
            try {
                throw null
            } catch {
                return true
            }
            return false
        """.trimIndent()

        val execution = ExecutionBenchmark("tryCatch", Source(code))

        assertEquals(LTrue, execution.result, "Code should produce true")
    }

    @Test
    fun fib() {
        val code = """
            fun fib(value) {
              var a = 1
              var b = 0
              var c = 0

              for (i in 1..(value - 1)) {
                c = b
                b = a
                a = c + b
              }

              return a
            }

            fib(10)
        """.trimIndent()

        val execution = ExecutionBenchmark("fib", Source(code))

        assertEquals(LInteger(55), execution.result, "Code should produce true")
    }
}
