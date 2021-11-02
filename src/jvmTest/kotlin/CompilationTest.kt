//import net.notjustanna.lin.compiler.NodeCompiler
//import net.notjustanna.lin.lexer.linStdLexer
//import net.notjustanna.lin.parser.linStdParser
//import net.notjustanna.tartar.api.lexer.Source
//
//fun main() {
//    val text = """
//        for (value in [1, 1L, 2f, 2.0, true, 'a', "abc"]) {
//            println(value)
//        }
//    """.trimIndent()
//
//    val node = linStdParser.parse(Source(text), linStdLexer)
//
//    val compiler = NodeCompiler()
//    node.accept(compiler)
//
//
//    val compiled = compiler.compiledSource()
//
////    val fromBytes = CompiledSource.fromBytes(compiled.toBytes())
//
////    println(fromBytes == compiled)
//
//    println("BINARY: " + compiled.toBytes().base64())
////    println(fromBytes.toBytes().hex())
//
//    println("STRING POOL: " + compiled.stringPool)
//    println("LONG POOL: " + compiled.longPool)
//    println("-------------------------")
//
//    val vm = LinVM(compiled, rootScope = testScope())
//    //var i = 0
//    // do {
//    //    println("Step ${i++} -- ${vm.currentNode.instructions.getOrNull(vm.next)}")
//    // }
//    while (vm.step());
//}
