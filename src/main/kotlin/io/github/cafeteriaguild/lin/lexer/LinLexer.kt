package io.github.cafeteriaguild.lin.lexer

import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.createLexer
import net.notjustanna.tartar.extensions.*
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
    '+' { process(makeToken(PLUS)) }
    "->" { process(makeToken(ARROW, 2)) }
    '-' { process(makeToken(MINUS)) }
    '*' { process(makeToken(ASTERISK)) }
    "//" { while (hasNext()) if (next() == '\n') break }
    "/*" { while (hasNext()) if (next() == '*' && match('/')) break }
    '/' { process(makeToken(F_SLASH)) }
    '\\' { process(makeToken(B_SLASH)) }
    "!=" { process(makeToken(NEQ, 2)) }
    '!' { process(makeToken(BANG)) }
    "?:" { process(makeToken(ELVIS)) }
    '?' { process(makeToken(QUESTION)) }
    "==" { process(makeToken(EQ, 2)) }
    '=' { process(makeToken(ASSIGN)) }
    "||" { process(makeToken(OR, 2)) }
    "&&" { process(makeToken(AND, 2)) }
    "<=" { process(makeToken(LTE, 2)) }
    '<' { process(makeToken(LT)) }
    ">=" { process(makeToken(GTE, 2)) }
    '>' { process(makeToken(GT)) }
    '\'' { process(makeToken(CHAR, readString(it))) }
    '"' { process(makeToken(STRING, readString(it))) }
    matching { it.isDigit() }.configure {
        process(
            when (val n = readNumber(it)) {
                is LexicalNumber.Invalid -> makeToken(INVALID, n.string)
                is LexicalNumber.Decimal -> makeToken(if (n.isFloat) FLOAT else DOUBLE, n.value.toString())
                is LexicalNumber.Integer -> makeToken(if (n.isLong) LONG else INT, n.value.toString())
            }
        )
    }
    matching { it.isLetter() || it == '_' }.configure {
        process(
            when (val s = readIdentifier(it)) {
                "use" -> makeToken(USE, 3)
                "true" -> makeToken(TRUE, 4)
                "false" -> makeToken(FALSE, 4)
                "val" -> makeToken(VAL, 3)
                "var" -> makeToken(VAR, 3)
                "mut" -> makeToken(MUT, 3)
                "return" -> makeToken(RETURN, 6)
                "mod" -> makeToken(MOD, 3)
                "if" -> makeToken(IF, 2)
                "else" -> makeToken(ELSE, 4)
                "null" -> makeToken(NULL, 4)
                "type" -> makeToken(TYPE, 4)
                "fun" -> makeToken(FUN, 3)
                "external" -> makeToken(EXTERNAL, 8)
                "struct" -> makeToken(STRUCT, 6)
                "break" -> makeToken(BREAK, 5)
                "while" -> makeToken(WHILE, 5)
                "is" -> makeToken(IS, 2)
                else -> makeToken(IDENT, s)
            }
        )
    }
    configure { process(makeToken(INVALID, it.toString())) }
}
