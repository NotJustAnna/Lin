package io.github.cafeteriaguild.lin

import net.notjustanna.tartar.api.lexer.Source
import io.github.cafeteriaguild.lin.ast.ASTViewer
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import io.github.cafeteriaguild.lin.parser.linStdParser
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.LinRuntime
import io.github.cafeteriaguild.lin.rt.scope.UserScope

fun main() {
    val source = Source(
        """
        thread {
            println("1 is run on ${'$'}threadName ${'$'}{millis()}")
        }
        thread {
            println("2 is run on ${'$'}threadName ${'$'}{millis()}")
        }
        thread {
            println("3 is run on ${'$'}threadName ${'$'}{millis()}")
        }
        """.trimIndent()
    )
    val expr = linStdParser.parse(source, linStdLexer)
    println(expr is Expr)
    println(buildString {
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
    val scope = UserScope()
    scope["millis"] = LinRuntime.millis
    scope["nanos"] = LinRuntime.nanos
    scope.declareProperty("threadName", LinRuntime.getThreadName)
    scope["thread"] = LinRuntime.runOnThread
    scope["println"] = LinRuntime.printlnConsole
    try {
        println("RESULT:\n    ${LinInterpreter().execute(expr, scope)}")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}