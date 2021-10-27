import net.notjustanna.lin.ast.viewer.NodeTreePrinter
import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linOptParser
import net.notjustanna.tartar.api.lexer.Source

fun main() {
    val text = """
        [1, 1L, 2f, 2.0, true, 'a', "abc"]
        
        {
            "id": 12345678,
            "name": "Jane Doe",
            "work": [{
                "employer": "Doe Inc.",
                "isFullTime": true
            }]
        }
        
        {
            id, 1: false, false: true, [propertyName]: propertyValue
        }
        
        null
    """.trimIndent()

    val node = linOptParser.parse(Source(text), linStdLexer)

    println(buildString {
        node.accept(NodeTreePrinter(this))
    })
}
