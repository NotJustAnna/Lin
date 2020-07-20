package io.github.cafeteriaguild.lin

import net.notjustanna.tartar.api.lexer.Source
import io.github.cafeteriaguild.lin.ast.ASTViewer
import io.github.cafeteriaguild.lin.ast.node.Expr
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import io.github.cafeteriaguild.lin.parser.linStdParser
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.LinRuntime
import io.github.cafeteriaguild.lin.rt.lib.LCallable
import io.github.cafeteriaguild.lin.rt.scope.UserScope

fun main() {
    val source = Source(
        """
        fun listOf(vararg values) = values
        
        println(nanos())
        var i = 0
        val a = listOf(1)
        a[i++] += ++i
        println(nanos())
        println(a + " " + i)
        
        object native {
            class Screen {
                var x = 0
                var y = 0
                object Size {
                    val w = 854
                    val h = 480
                }
            }
            val prefix = "haha "
            fun toJson() = prefix + "yesn't"
            fun fromJson() = prefix + "no"
        }
        val screen = native.Screen()
        screen.x = 5
        return screen.x + " of " + native.Screen.Size.w
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
        val execute = LinInterpreter().execute(expr, scope)
        println("RESULT:\n    $execute")
        if (execute is LCallable) {
            println(execute.invoke(emptyList()))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}