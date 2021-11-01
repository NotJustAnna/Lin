import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.compiler.NodeCompiler
import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linStdParser
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

    val fromBytes = CompiledSource.fromBytes(compiled.toBytes())

    println(fromBytes == compiled)

    println(compiled.toBytes().hex())
    println(fromBytes.toBytes().hex())
}
