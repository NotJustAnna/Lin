//package io.github.cafeteriaguild.lin
//
//import net.notjustanna.tartar.api.lexer.Source
//import io.github.cafeteriaguild.lin.ast.ASTViewer
//import io.github.cafeteriaguild.lin.lexer.linStdLexer
//import io.github.cafeteriaguild.lin.parser.linStdParser
//import io.github.cafeteriaguild.lin.rt.LinRuntime
//import io.github.cafeteriaguild.lin.rt.lib.LObj
//import io.github.cafeteriaguild.lin.rt.lib.LString
//import io.github.cafeteriaguild.lin.rt.scope.FunctionScope
//import io.github.cafeteriaguild.lin.rt.types.LTClass
//import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
//
//fun main() {
//    val src = Source("bios")
//    //val list = linStdLexer.parseToList(src)
//    //println(list)
//    val ast = linStdParser.parse(src, linStdLexer)
//    println(buildString { ast.accept(ASTViewer(this, "", true)) })
//
//    val scope = FunctionScope()
//    scope.declareLocalProperty("bios", false).set(LObj(object : LTClass.Base() {
//        override val name: String = "cc.BasicInputOutputSystem"
//        override val memberFunctions: Map<String, LFunction> = mapOf(
//            "toString" to object : LFunction {
//                override fun call(receiver: LObj, params: List<LObj>): LObj {
//                    return LString("BIOS")
//                }
//            }
//        )
//    }))
//
//    println("Done |> " + ast.accept(LinRuntime(), scope))
//}