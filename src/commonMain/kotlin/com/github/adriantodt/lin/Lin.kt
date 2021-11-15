package com.github.adriantodt.lin

import com.github.adriantodt.lin.lexer.linStdLexer
import com.github.adriantodt.lin.parser.LinSourceParser
import com.github.adriantodt.lin.parser.linStdGrammar
import com.github.adriantodt.lin.parser.linStdParser
import com.github.adriantodt.tartar.api.parser.SourceParser

public object Lin {
    public val parser: LinSourceParser = SourceParser(linStdLexer(), linStdParser(linStdGrammar()))
}
