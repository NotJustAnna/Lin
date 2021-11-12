package net.notjustanna.lin.test.regression.lexer

import net.notjustanna.lin.Lin
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.SyntaxException
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class LexerRegressionTests {
    @Test
    fun unterminatedUnicodeLiteral() {
        val code = """
            "\u123
        """.trimIndent()
        val throwable = assertFails {
            Lin.parser.lexer.parseToList(Source(code, "unterminatedUnicodeLiteral.lin"))
        }
        throwable.printStackTrace()
        assertIs<SyntaxException>(throwable)
        val section = throwable.section
        assertNotNull(section)

        println(section)
        println(section.substring)
    }
}
