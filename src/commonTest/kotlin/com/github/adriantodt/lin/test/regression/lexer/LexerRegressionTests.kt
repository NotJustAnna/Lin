package com.github.adriantodt.lin.test.regression.lexer

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.SyntaxException
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
