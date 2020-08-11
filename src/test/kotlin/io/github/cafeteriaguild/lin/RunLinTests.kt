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
import java.io.File

fun main() {
    val source = Source(File("examples/fib.lin"))
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
    scope["threadName"] = LinRuntime.threadName
    scope["thread"] = LinRuntime.runOnThread
    scope["println"] = LinRuntime.printlnConsole
    try {
        val execute = LinInterpreter().execute(expr, scope)
        println("RESULT:\n    $execute")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}