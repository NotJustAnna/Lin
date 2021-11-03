package net.notjustanna.lin

import net.notjustanna.lin.grammar.linStdGrammar
import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.linStdParser

object Lin {
    val lexer = linStdLexer()

    val grammar = linStdGrammar()

    val parser = linStdParser(grammar)
}
