package com.github.adriantodt.lin.test.regression.lexer

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.SyntaxException
import kotlin.test.*

class LexerRegressionTests {
    @Test
    fun unterminatedUnicodeLiteral() {
        val code = """
            "\u123
        """.trimIndent()
        val throwable = assertFails {
            Lin.parser.lexer.parseToList(Source(code, "unterminatedUnicodeLiteral.lin"))
        }
        assertIs<SyntaxException>(throwable)
        val section = throwable.section
        assertNotNull(section)
        assertEquals("\\u123", section.substring)
    }

    @Test
    fun unterminatedString() {
        val code = """
            "ABC
        """.trimIndent()
        val throwable = assertFails {
            Lin.parser.lexer.parseToList(Source(code, "unterminatedString.lin"))
        }
        assertIs<SyntaxException>(throwable)
        val section = throwable.section
        assertNotNull(section)
        assertEquals("\"ABC", section.substring)
    }
}
