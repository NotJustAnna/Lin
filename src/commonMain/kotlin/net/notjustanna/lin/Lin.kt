package net.notjustanna.lin

import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linStdGrammar
import net.notjustanna.lin.parser.linStdParser
import net.notjustanna.tartar.api.parser.SourceParser

object Lin {
    val parser = SourceParser(linStdLexer(), linStdParser(linStdGrammar()))
}
