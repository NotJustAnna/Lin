package io.github.cafeteriaguild.lin.oldrt//package io.github.cafeteriaguild.lin
//
//import net.notjustanna.tartar.api.lexer.Source
//import io.github.cafeteriaguild.lin.lexer.linStdLexer
//import io.github.cafeteriaguild.lin.parser.linStdParser
//import io.github.cafeteriaguild.lin.rt.scope.FunctionScope
//import java.util.concurrent.LinkedBlockingQueue
//
//fun main() {
//    val sources = listOf(
//        "1; 2; 3; 4; 5; 6; 7; 8; 9; 10",
//        "1; 2; 3; 4; return 5; 6; 7; 8; 9; 10",
//        "10; 9; 8; 7; 6; 5; 4; 3; 2; 1",
//        "10; 9; return 8; 7; 6; 5; 4; 3; 2; 1"
//    )
//
//    val continuations = sources.mapIndexedTo(LinkedBlockingQueue()) { i, src ->
//        i to linStdParser.parse(Source(src, "sources/$i"), linStdLexer).accept(LinOldRuntime(), FunctionScope(null))
//    }
//
//    while (continuations.isNotEmpty()) {
//        val (i, c) = continuations.poll()
//        val next = c.next()
//        if (next.hasNext()) {
//            continuations.offer(i to next)
//        } else {
//            println("Done #$i |> ${next.done()}")
//        }
//    }
//}