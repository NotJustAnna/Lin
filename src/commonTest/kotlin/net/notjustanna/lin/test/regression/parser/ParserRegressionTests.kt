package net.notjustanna.lin.test.regression.parser

import net.notjustanna.lin.Lin
import net.notjustanna.lin.validator.NodeValidator
import net.notjustanna.tartar.api.lexer.Source
import kotlin.test.Test
import kotlin.test.assertNotNull

class ParserRegressionTests {
    @Test
    fun functionExceededMaxParameters() {
        val code = """
            fun test(${(0..256).joinToString { "p$it" }}) {}
        """.trimIndent()

        val node = Lin.parser.parse(Source(code))
        assertNotNull(node.accept(NodeValidator))
    }

    @Test
    fun functionExceededMaxVarargs() {
        val code = """
            fun test(vararg a, vararg b) {}
        """.trimIndent()

        val node = Lin.parser.parse(Source(code))
        assertNotNull(node.accept(NodeValidator))
    }
}
