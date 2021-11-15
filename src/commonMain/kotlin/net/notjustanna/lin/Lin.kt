package net.notjustanna.lin

import net.notjustanna.lin.lexer.linStdLexer
import net.notjustanna.lin.parser.LinSourceParser
import net.notjustanna.lin.parser.linStdGrammar
import net.notjustanna.lin.parser.linStdParser
import net.notjustanna.tartar.api.parser.SourceParser

public object Lin {
    public val parser: LinSourceParser = SourceParser(linStdLexer(), linStdParser(linStdGrammar()))
}
