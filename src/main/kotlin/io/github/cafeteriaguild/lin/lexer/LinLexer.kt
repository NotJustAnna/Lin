package io.github.cafeteriaguild.lin.lexer

import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.createLexer
import net.notjustanna.tartar.extensions.LexicalNumber
import net.notjustanna.tartar.extensions.makeToken
import net.notjustanna.tartar.extensions.readNumber
import net.notjustanna.tartar.extensions.readString
import io.github.cafeteriaguild.lin.lexer.TokenType.*

val linStdLexer = createLexer<Token<TokenType>> {
    ' '()
    '\r' {
        match('\n')
        process(makeToken(NL))
    }
    '\n' { process(makeToken(NL)) }
    '{' { process(makeToken(L_BRACE)) }
    '}' { process(makeToken(R_BRACE)) }
    '(' { process(makeToken(L_PAREN)) }
    ')' { process(makeToken(R_PAREN)) }
    '[' { process(makeToken(L_BRACKET)) }
    ']' { process(makeToken(R_BRACKET)) }
    ".." { process(makeToken(RANGE, 2)) }
    '.' { process(makeToken(DOT)) }
    ',' { process(makeToken(COMMA)) }
    "::" { process(makeToken(DOUBLE_COLON, 2)) }
    ':' { process(makeToken(COLON)) }
    ';' { process(makeToken(SEMICOLON)) }
    "+=" { process(makeToken(PLUS_ASSIGN, 2)) }
    "++" { process(makeToken(INCREMENT, 2)) }
    '+' { process(makeToken(PLUS)) }
    "->" { process(makeToken(ARROW, 2)) }
    "-=" { process(makeToken(MINUS_ASSIGN, 2)) }
    "--" { process(makeToken(DECREMENT, 2)) }
    '-' { process(makeToken(MINUS)) }
    "%=" { process(makeToken(REM_ASSIGN, 2)) }
    '%' { process(makeToken(REM)) }
    "*=" { process(makeToken(ASTERISK_ASSIGN, 2)) }
    '*' { process(makeToken(ASTERISK)) }
    "//" { while (hasNext()) if (next() == '\n') break }
    "/*" { while (hasNext()) if (next() == '*' && match('/')) break }
    "/=" { process(makeToken(SLASH_ASSIGN, 2)) }
    '/' { process(makeToken(SLASH)) }
    '\\' { process(makeToken(BACKSLASH)) }
    "!=" { process(makeToken(NEQ, 2)) }
    "!!" { process(makeToken(DOUBLE_BANG, 2)) }
    '!' { process(makeToken(BANG)) }
    "?:" { process(makeToken(ELVIS)) }
    "?." { process(makeToken(QUESTION_DOT, 2)) }
    '?' { process(makeToken(QUESTION)) }
    "==" { process(makeToken(EQ, 2)) }
    '=' { process(makeToken(ASSIGN)) }
    "||" { process(makeToken(OR, 2)) }
    "|" { process(makeToken(PIPE)) }
    "&&" { process(makeToken(AND, 2)) }
    "&" { process(makeToken(AMP)) }
    "<=" { process(makeToken(LTE, 2)) }
    '<' { process(makeToken(LT)) }
    ">=" { process(makeToken(GTE, 2)) }
    '>' { process(makeToken(GT)) }
    '\'' { process(makeToken(CHAR, readLinString(it))) }
    "\"\"\"" { readLinTemplateString(it.toString(), true) }
    "\"\"" { process(makeToken(STRING, 2)) }
    '"' { readLinTemplateString(it.toString(), false) }
    "`" { process(makeToken(IDENTIFIER, readString(it))) }
    matching { it.isDigit() }.configure {
        process(
            when (val n = readNumber(it)) {
                is LexicalNumber.Invalid -> makeToken(INVALID, n.string)
                is LexicalNumber.Decimal -> makeToken(if (n.isFloat) FLOAT else DOUBLE, n.value.toString())
                is LexicalNumber.Integer -> makeToken(if (n.isLong) LONG else INT, n.value.toString())
            }
        )
    }
    matching { it.isLetter() || it == '_' || it == '@' }.configure {
        process(
            when (val s = readLinIdentifier(it)) {
                // Kotlin keywords
                "as" -> makeToken(AS, 2)
                "break" -> makeToken(BREAK, 5)
                "class" -> makeToken(CLASS, 5)
                "continue" -> makeToken(CONTINUE, 8)
                "do" -> makeToken(DO, 2)
                "else" -> makeToken(ELSE, 4)
                "false" -> makeToken(FALSE, 4)
                "for" -> makeToken(FOR, 3)
                "fun" -> makeToken(FUN, 3)
                "if" -> makeToken(IF, 2)
                "in" -> makeToken(IN, 2)
                "interface" -> makeToken(INTERFACE, 9)
                "is" -> makeToken(IS, 2)
                "null" -> makeToken(NULL, 4)
                "object" -> makeToken(OBJECT, 6)
                "package" -> makeToken(PACKAGE, 7)
                "return" -> makeToken(RETURN, 6)
                "super" -> makeToken(SUPER, 5)
                "this" -> makeToken(THIS, 4)
                "throw" -> makeToken(THROW, 5)
                "true" -> makeToken(TRUE, 4)
                "try" -> makeToken(TRY, 3)
                "typealias" -> makeToken(TYPEALIAS, 9)
                "typeof" -> makeToken(TYPEOF, 6)
                "val" -> makeToken(VAL, 3)
                "var" -> makeToken(VAR, 3)
                "when" -> makeToken(WHEN, 4)
                "while" -> makeToken(WHILE, 5)
                // Lin keywords
                "import" -> makeToken(IMPORT, 6)
                else -> makeToken(IDENTIFIER, s)
            }
        )
    }
    configure { process(makeToken(INVALID, next().toString())) }
}
