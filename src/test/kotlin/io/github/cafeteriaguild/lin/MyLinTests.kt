package io.github.cafeteriaguild.lin

import net.notjustanna.tartar.api.lexer.Source
import io.github.cafeteriaguild.lin.ast.ASTViewer
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import io.github.cafeteriaguild.lin.parser.linStdParser

fun main() {
    val source = Source(
        """
        interface Marker
        class Any
        interface B {
            fun test(vararg values)
            
            companion object C {
                val value = "hi"
                
                fun test(vararg values) {
                    values.toList()
                }
            }
        }
        class A {
            companion object {
                val test = "hi"
            }
        }
//        companion object {
//            val hw = getHardware()
//        }
//        hw.interrupt { | i, (event, data) | ->
//            println("${'$'}event ${'$'}data ${'$'}i")
//        }
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