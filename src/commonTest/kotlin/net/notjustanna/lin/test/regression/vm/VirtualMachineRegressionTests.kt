package net.notjustanna.lin.test.regression.vm

import net.notjustanna.lin.compiler.NodeCompiler
import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linStdParser
import net.notjustanna.lin.test.utils.TestScope
import net.notjustanna.lin.vm.LinVM
import net.notjustanna.lin.vm.types.LDecimal
import net.notjustanna.lin.vm.types.LInteger
import net.notjustanna.lin.vm.types.LString
import net.notjustanna.lin.vm.types.LTrue
import net.notjustanna.tartar.api.lexer.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VirtualMachineRegressionTests {
    @Test
    fun forLoop() {
        val array = listOf(
            LInteger(1), LInteger(1), LDecimal(2.0),
            LDecimal(2.0), LTrue, LString("a"), LString("abc")
        )
        val code = """
            for (value in [1, 1L, 2f, 2.0, true, 'a', "abc"]) {
                publish(value)
            }
        """.trimIndent()

        val compiler = NodeCompiler()
        linStdParser.parse(Source(code), linStdLexer).accept(compiler)
        val compiledSource = compiler.sourceBuilder.build()

        val testScope = TestScope()

        val vm = LinVM(compiledSource, rootScope = testScope.scope)
        while (vm.step());
        assertNull(vm.result, "Code should not produce result.")

        for (any in array) {
            assertEquals(any, testScope.output.removeFirst())
        }
    }

    @Test
    fun variables() {
        val array = listOf(
            LInteger(1), LInteger(2),
        )
        val code = """
            val a = 1
            val b = 2
            publish(a, b)
        """.trimIndent()

        val compiler = NodeCompiler()
        linStdParser.parse(Source(code), linStdLexer).accept(compiler)
        val compiledSource = compiler.sourceBuilder.build()

        val testScope = TestScope()

        val vm = LinVM(compiledSource, rootScope = testScope.scope)
        while (vm.step());
        assertNull(vm.result, "Code should not produce result.")

        for (any in array) {
            assertEquals(any, testScope.output.removeFirst())
        }
    }
}
