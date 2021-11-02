//import net.notjustanna.lin.lexer.linStdLexer
//import net.notjustanna.lin.parser.linStdParser
//import net.notjustanna.tartar.api.lexer.Source
//
//fun main() {
//    val text = """
//        [1, 1L, 2f, 2.0, true, 'a', "abc"]
//
//        {
//            "id": 12345678,
//            "name": "Jane Doe",
//            "work": [{
//                "employer": "Doe Inc.",
//                "isFullTime": true
//            }]
//        }
//
//        {
//            id, 1: false, false: true, [propertyName]: propertyValue
//        }
//    """.trimIndent()
//
//    println(linStdParser.parse(Source(text), linStdLexer))
//}
