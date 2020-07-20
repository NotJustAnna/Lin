package io.github.cafeteriaguild.lin

import net.notjustanna.tartar.api.lexer.Source
import io.github.cafeteriaguild.lin.ast.ASTViewer
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import io.github.cafeteriaguild.lin.parser.linStdParser

fun main() {
    val source = Source(
        """
        val was = millis()
        val now = millis()
        was - now
        """.trimIndent()
    )
    println(buildString {
        val expr = linStdParser.parse(source, linStdLexer)
        println(expr is Expr)
        expr.accept(ASTViewer(this, "", true))
        if (expr is InvalidNode) {
            for (child in expr.children.filterIsInstance<InvalidNode>()) {
                for (error in child.errors) {
                    error.printStackTrace()
                }
            }
            for (error in expr.errors) {
                error.printStackTrace()
            }
        }
    })
}