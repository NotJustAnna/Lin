package com.github.adriantodt.lin

import com.github.adriantodt.lin.lexer.linStdLexer
import com.github.adriantodt.lin.parser.linStdGrammar
import com.github.adriantodt.lin.parser.linStdParser
import com.github.adriantodt.tartar.api.parser.SourceParser

object Lin {
    val parser = SourceParser(linStdLexer(), linStdParser(linStdGrammar()))
}
