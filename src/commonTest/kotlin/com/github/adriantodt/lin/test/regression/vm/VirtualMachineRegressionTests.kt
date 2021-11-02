package com.github.adriantodt.lin.test.regression.vm

import com.github.adriantodt.lin.compiler.NodeCompiler
import com.github.adriantodt.lin.lexer.linStdLexer
import com.github.adriantodt.lin.parser.linStdParser
import com.github.adriantodt.lin.test.utils.TestScope
import com.github.adriantodt.lin.vm.LinVM
import com.github.adriantodt.lin.vm.types.LDecimal
import com.github.adriantodt.lin.vm.types.LInteger
import com.github.adriantodt.lin.vm.types.LString
import com.github.adriantodt.lin.vm.types.LTrue
import com.github.adriantodt.tartar.api.lexer.Source
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
