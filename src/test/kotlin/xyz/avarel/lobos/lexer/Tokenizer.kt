package xyz.avarel.lobos.lexer

import java.io.Reader

class Tokenizer(val source: Source) {
    private val reader: Reader = source.reader()

    private var lineNumber: Int = 1
    private var lineIndex: Int = 0

    fun parse() = mutableListOf<Token>().also(::parseTo)

    private fun parseTo(list: MutableList<Token>) {
        while (hasNext()) {
            parseCharTo(list, next())
        }
    }

    private fun parseCharTo(list: MutableList<Token>, c: Char) {
        when (c) {
            '{' -> list += makeToken(TokenType.L_BRACE)
            '}' -> list += makeToken(TokenType.R_BRACE)
            '(' -> list += makeToken(TokenType.L_PAREN)
            ')' -> list += makeToken(TokenType.R_PAREN)
            '[' -> list += makeToken(TokenType.L_BRACKET)
            ']' -> list += makeToken(TokenType.R_BRACKET)
            '_' -> list += makeToken(TokenType.UNDERSCORE)
            '.' -> when {
                match('.') -> when {
                    match('=') -> list += makeToken(TokenType.RANGE_IN, 3)
                    match('<') -> list += makeToken(TokenType.RANGE_EX, 3)
                }
                else -> list += makeToken(TokenType.DOT)
            }
            ',' -> list += makeToken(TokenType.COMMA)
            ':' -> list += when {
                match(':') -> makeToken(TokenType.DOUBLE_COLON)
                else -> makeToken(TokenType.COLON)
            }
            ';' -> list += makeToken(TokenType.SEMICOLON)
            '\r' -> {
                match('\n')
                list += makeToken(TokenType.NL)
            }
            '\n' -> list += makeToken(TokenType.NL)
            '+' -> list += makeToken(TokenType.PLUS)
            '-' -> list += when {
                match('>') -> makeToken(TokenType.ARROW, 2)
                else -> makeToken(TokenType.MINUS)
            }
            '*' -> list += makeToken(TokenType.ASTERISK)
            '/' -> list += makeToken(TokenType.F_SLASH)
            '\\' -> list += makeToken(TokenType.B_SLASH)
            '!' -> list += when {
                match('=') -> makeToken(TokenType.NEQ, 2)
                else -> makeToken(TokenType.BANG)
            }
            '?' -> list += makeToken(TokenType.QUESTION)
            '=' -> list += when {
                match('=') -> makeToken(TokenType.EQ, 2)
                else -> makeToken(TokenType.ASSIGN)
            }
            '|' -> list += when {
                match('|') -> makeToken(TokenType.OR, 2)
                match('>') -> makeToken(TokenType.PIPE_FORWARD, 2)
                else -> makeToken(TokenType.PIPE)
            }
            '&' -> list += when {
                match('&') -> makeToken(TokenType.AND, 2)
                else -> makeToken(TokenType.AMP)
            }
            '<' -> list += when {
                match('=') -> makeToken(TokenType.LTE, 2)
                else -> makeToken(TokenType.LT)
            }
            '>' -> list += when {
                match('=') -> makeToken(TokenType.GTE, 2)
                else -> makeToken(TokenType.GT)
            }
            '"' -> parseStringTo(list, '"', true)
            '\'' -> parseStringTo(list, '\'', false)
            ' ' -> Unit
            else -> when {
                c.isDigit() -> parseNumberTo(list, c)
                c.isLetter() -> parseIdentTo(list, c)
                else -> list += makeToken(TokenType.INVALID, c.toString())
            }
        }
    }

    private fun makeToken(tokenType: TokenType, offset: Int = 1) = makeToken(tokenType, "", offset)
    private fun makeToken(tokenType: TokenType, string: String, offset: Int = 0) = Token(
        tokenType,
        string,
        Section(source, lineNumber, lineIndex - string.length - offset, string.length + offset)
    )

    private fun parseIdentTo(list: MutableList<Token>, c: Char) {
        val buf = StringBuilder()
        buf.append(c)

        while (hasNext()) {
            val cc = peek()
            if (cc.isLetterOrDigit() || cc == '_') {
                buf.append(cc)
                next()
            } else {
                break
            }
        }

        val str = buf.toString()
        list += when (str) {
            "use" -> makeToken(TokenType.USE, 3)
            "true" -> makeToken(TokenType.TRUE, 4)
            "false" -> makeToken(TokenType.FALSE, 4)
            "let" -> makeToken(TokenType.LET, 3)
            "mut" -> makeToken(TokenType.MUT, 3)
            "return" -> makeToken(TokenType.RETURN, 6)
            "mod" -> makeToken(TokenType.MOD, 3)
            "if" -> makeToken(TokenType.IF, 2)
            "else" -> makeToken(TokenType.ELSE, 4)
            "null" -> makeToken(TokenType.NULL, 4)
            "type" -> makeToken(TokenType.TYPE, 4)
            "def" -> makeToken(TokenType.DEF, 3)
            "external" -> makeToken(TokenType.EXTERNAL, 8)
            "struct" -> makeToken(TokenType.STRUCT, 6)
            "break" -> makeToken(TokenType.BREAK, 5)
            "while" -> makeToken(TokenType.WHILE, 5)
            "is" -> makeToken(TokenType.IS, 2)
            else -> makeToken(TokenType.IDENT, str)
        }
    }

    private fun parseStringTo(list: MutableList<Token>, delim: Char, template: Boolean) {
        var buf = StringBuilder()
        var eol = false

        while (hasNext()) {
            val c = peek()
            if (c == '$' && template) {
                next()

                if (peek() == '{') {
                    next()

                    list += makeToken(TokenType.STRING, buf.toString())
                    list += makeToken(TokenType.PLUS)
                    buf = StringBuilder()

                    var braces = 0

                    list += makeToken(TokenType.L_PAREN)

                    while (hasNext()) {
                        val cc = next()
                        if (cc == '}') {
                            if (braces == 0) {
                                break
                            } else {
                                braces--
                                parseCharTo(list, cc)
                            }
                        } else if (cc == '{') {
                            braces++
                            parseCharTo(list, cc)
                        } else parseCharTo(list, cc)
                    }

                    list += makeToken(TokenType.R_PAREN)
                    list += makeToken(TokenType.PLUS)
                } else if (peek().isLetter()) {
                    list += makeToken(TokenType.STRING, buf.toString())
                    list += makeToken(TokenType.PLUS)
                    buf = StringBuilder()

                    buf.append(next())

                    while (hasNext() && peek().isLetterOrDigit()) {
                        buf.append(next())
                    }

                    list += makeToken(TokenType.IDENT, buf.toString())
                    buf = StringBuilder()

                    list += makeToken(TokenType.PLUS)
                } else {
                    buf.append(next())
                }
            } else if (c == delim) {
                next()
                eol = true
                break
            } else {
                next()
                buf.append(c)
            }
        }

        if (!eol) {
            throw IllegalStateException("Unterminated string")
        }

        if (buf.isEmpty() && list.last().type == TokenType.PLUS) {
            list.removeAt(list.lastIndex)
        } else {
            list += makeToken(TokenType.STRING, buf.toString(), 2)
        }
    }

    private fun parseNumberTo(list: MutableList<Token>, c: Char) {
        val buf = StringBuilder()

        if (c == '0') {
            when {
                match('x') -> {
                    fillBufferNumbers(buf, true)

                    val numberStr = buf.toString().toIntOrNull(16)?.toString()

                    if (numberStr == null) {
                        list += makeToken(TokenType.INVALID, buf.toString())
                        return
                    }

                    list += makeToken(TokenType.INT, numberStr)
                    return
                }
                match('b') -> {
                    fillBufferNumbers(buf, false)

                    val numberStr = buf.toString().toIntOrNull(2)?.toString()

                    if (numberStr == null) {
                        list += makeToken(TokenType.INVALID, buf.toString())
                        return
                    }

                    list += makeToken(TokenType.INT, numberStr)
                    return
                }
                else -> {
                    buf.append('0')
                }
            }
        } else {
            buf.append(c)
        }

        fillBufferNumbers(buf, false)

        list += when {
            peek() == '.' && peek(1).isDigit() -> {
                next()
                buf.append('.')
                fillBufferNumbers(buf, false)
                makeToken(TokenType.DECIMAL, buf.toString())
            }
            else -> {
                makeToken(TokenType.INT, buf.toString())
            }
        }
    }

    private fun fillBufferNumbers(buf: StringBuilder, allowHex: Boolean) {
        while (hasNext()) {
            val c = peek()
            if (c.isDigit() || (allowHex && c in 'A'..'F')) {
                buf.append(next())
            } else {
                break
            }
        }
    }

    private fun peek(): Char {
        reader.mark(1)
        val c = reader.read().toChar()
        reader.reset()
        return c
    }

    private fun peek(distance: Int): Char {
        reader.mark(distance + 1)
        val array = CharArray(distance + 1)
        val result = when {
            reader.read(array) < distance + 1 -> (-1).toChar()
            else -> array[distance]
        }
        reader.reset()
        return result
    }

    private fun peekString(length: Int): String {
        val array = CharArray(length)
        reader.mark(length)
        val len = reader.read(array)
        reader.reset()
        return when {
            len == -1 -> ""
            len < length -> String(array.copyOf(len))
            else -> String(array)
        }
    }

    private fun match(c: Char): Boolean {
        return if (peek() == c) {
            next()
            true
        } else {
            false
        }
    }

    private fun hasNext(): Boolean {
        reader.mark(1)
        val i = reader.read()
        reader.reset()
        return i > 0
    }

    private fun next(): Char {
        val c = reader.read().toChar()

        when (c) {
            '\n' -> {
                lineNumber++
                lineIndex = 0
            }
            else -> {
                lineIndex++
            }
        }

        return c
    }

    private fun nextString(length: Int): String {
        val buf = StringBuilder(length)
        var i = 0
        while (hasNext() && i++ < length) {
            buf.append(next())
        }
        return buf.toString()
    }
}

