package io.github.cafeteriaguild.lin

import net.notjustanna.tartar.api.lexer.Source
import io.github.cafeteriaguild.lin.ast.ASTViewer
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import io.github.cafeteriaguild.lin.parser.linStdParser

fun main() {
    val source = Source(
        """
        a = b
        1 + 2 * 3 - 4 / 5
    """.trimIndent()
    )

    println(buildString {
        linStdParser.parse(source, linStdLexer).accept(ASTViewer(this, "", true))
    })
}