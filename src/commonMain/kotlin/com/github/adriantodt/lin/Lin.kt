package com.github.adriantodt.lin

import com.github.adriantodt.lin.grammar.linStdGrammar
import com.github.adriantodt.lin.lexer.linStdLexer
import com.github.adriantodt.lin.parser.linStdParser

object Lin {
    val lexer = linStdLexer()

    val grammar = linStdGrammar()

    val parser = linStdParser(grammar)
}
