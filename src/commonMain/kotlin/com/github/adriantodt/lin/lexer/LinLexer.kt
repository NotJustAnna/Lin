package com.github.adriantodt.lin.lexer

import com.github.adriantodt.lin.lexer.TokenType.*
import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.LexicalNumber
import com.github.adriantodt.tartar.extensions.makeToken
import com.github.adriantodt.tartar.extensions.readNumber
import com.github.adriantodt.tartar.extensions.readString

typealias LinToken = Token<TokenType>

internal fun linStdLexer() = Lexer.create<LinToken> {
    ' ' { while (hasNext()) if (!match(' ')) break }
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
    '\u037E' { process(makeToken(SEMICOLON)) } // Greek question mark
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
    '\'' { readLinTemplateString(it.toString(), false) }
    "\"\"\"" { readLinTemplateString(it.toString(), true) }
    "\"\"" { process(makeToken(STRING, 2)) }
    '"' { readLinTemplateString(it.toString(), false) }
    "`" { process(makeToken(IDENTIFIER, readString(it))) }
    matching(CharPredicate.isDigit).configure {
        process(
            when (val n = readNumber(it)) {
                is LexicalNumber.Invalid -> makeToken(INVALID, n.string)
                is LexicalNumber.Decimal -> makeToken(DECIMAL, n.value.toString())
                is LexicalNumber.Integer -> makeToken(INTEGER, n.value.toString())
            }
        )
    }
    matching { it.isLetter() || it == '_' || it == '@' }.configure {
        process(
            when (val s = readLinIdentifier(it)) {
                "break" -> makeToken(BREAK, 5)
                "continue" -> makeToken(CONTINUE, 8)
                "do" -> makeToken(DO, 2)
                "else" -> makeToken(ELSE, 4)
                "false" -> makeToken(FALSE, 4)
                "for" -> makeToken(FOR, 3)
                "fun" -> makeToken(FUN, 3)
                "if" -> makeToken(IF, 2)
                "in" -> makeToken(IN, 2)
                "is" -> makeToken(IS, 2)
                "null" -> makeToken(NULL, 4)
                "return" -> makeToken(RETURN, 6)
                "this" -> makeToken(THIS, 4)
                "throw" -> makeToken(THROW, 5)
                "true" -> makeToken(TRUE, 4)
                "try" -> makeToken(TRY, 3)
                "typeof" -> makeToken(TYPEOF, 6)
                //"unit" -> makeToken(UNIT, 4)
                "val" -> makeToken(VAL, 3)
                "var" -> makeToken(VAR, 3)
                "when" -> makeToken(WHEN, 4)
                "while" -> makeToken(WHILE, 5)

                else -> makeToken(IDENTIFIER, s)
            }
        )
    }
    configure { process(makeToken(INVALID, next().toString())) }
}
