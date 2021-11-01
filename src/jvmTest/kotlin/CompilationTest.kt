import net.notjustanna.lin.compiler.NodeCompiler
import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linStdParser
import net.notjustanna.lin.vm.LinVM
import net.notjustanna.tartar.api.lexer.Source

fun main() {
    val text = """
        for (value in [1, 1L, 2f, 2.0, true, 'a', "abc"]) {
            println(value)
        }
    """.trimIndent()

    val node = linStdParser.parse(Source(text), linStdLexer)

    val compiler = NodeCompiler()
    node.accept(compiler)


    val compiled = compiler.sourceBuilder.build()

//    val fromBytes = CompiledSource.fromBytes(compiled.toBytes())

//    println(fromBytes == compiled)

    println("BINARY: " + compiled.toBytes().base64())
//    println(fromBytes.toBytes().hex())

    println("STRING POOL: " + compiled.stringPool)

    println("LONG POOL: " + compiled.longPool)

    val vm = LinVM(compiled)
    var i = 0
    while (true) {
        println("Step ${i++} -- ${vm.currentNode.instructions[vm.next]}")
        vm.step()
    }
}
