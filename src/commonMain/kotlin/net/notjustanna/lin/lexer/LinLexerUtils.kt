package net.notjustanna.lin.lexer

import net.notjustanna.tartar.api.lexer.LexerContext
import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.extensions.lexer.processToken
import net.notjustanna.tartar.extensions.lexer.section

fun LexerContext<*>.readLinIdentifier(firstChar: Char? = null): String {
    val buf = StringBuilder()
    firstChar?.let(buf::append)
    while (hasNext()) {
        val cc = peek()
        if (cc.isLetterOrDigit() || cc == '_' || cc == '@') {
            buf.append(cc)
            next()
        } else {
            break
        }
    }
    return buf.toString()
}

fun LexerContext<*>.readLinString(delimiter: Char): String {
    val buf = StringBuilder()
    var eol = false
    outer@ while (hasNext()) {
        val c = peek()
        if (c == delimiter) {
            next()
            eol = true
            break
        } else if (c == '\\') {
            next()
            if (!hasNext()) break
            when (val escapingChar = next()) {
                'n' -> buf.append('\n')
                'r' -> buf.append('\r')
                'b' -> buf.append('\b')
                't' -> buf.append('\t')
                '\'' -> buf.append('\'')
                '"' -> buf.append('"')
                '\\' -> buf.append('\\')
                'u' -> {
                    val u = nextString(4)
                    if (u.length != 4) break@outer
                    buf.append(u.toInt(16).toChar())
                }
                else -> throw SyntaxException("Unknown escaping '\\$escapingChar'", section(2))
            }
        } else {
            next()
            buf.append(c)
        }
    }
    if (!eol) {
        throw IllegalStateException("Unterminated String")
    }
    return buf.toString()
}


fun LexerContext<LinToken>.readLinTemplateString(delim: String, raw: Boolean) {
    val absoluteStart = index - delim.length
    var sectionOffset = delim.length
    var start = index
    val buf = StringBuilder()
    var eol = false

    while (hasNext()) {
        val c = peek()
        if (c == '$') {
            next()

            if (peek() == '{') {
                next()

                processToken(
                    TokenType.STRING, buf.toString(), index - start, index - start - 2, sectionOffset
                )
                processToken(TokenType.PLUS)
                sectionOffset = 0
                buf.clear()

                var braces = 0

                processToken(TokenType.L_PAREN)

                while (hasNext()) {
                    val cc = peek()
                    if (cc == '}') {
                        if (braces == 0) {
                            next()
                            break
                        } else {
                            braces--
                        }
                    } else if (cc == '{') {
                        braces++
                    }
                    parseOnce().forEach(this::process)
                }

                start = index
                processToken(TokenType.R_PAREN)
                processToken(TokenType.PLUS)
            } else if (peek().isLetter()) {
                processToken(
                    TokenType.STRING, buf.toString(), index - start, index - start - 1, sectionOffset
                )
                processToken(TokenType.PLUS)
                sectionOffset = 0
                buf.clear()

                buf.append(next())

                while (hasNext() && peek().isLetterOrDigit()) {
                    buf.append(next())
                }
                start = index

                processToken(TokenType.IDENTIFIER, buf.toString())
                buf.clear()

                processToken(TokenType.PLUS, 0)
            } else {
                buf.append(next())
            }
        } else if (c == '\\' && !raw) {
            next()
            if (!hasNext()) break
            when (next()) {
                'n' -> buf.append('\n')
                'r' -> buf.append('\r')
                'b' -> buf.append('\b')
                't' -> buf.append('\t')
                '\'' -> buf.append('\'')
                '"' -> buf.append('"')
                '\\' -> buf.append('\\')
                'u' -> {
                    val u = peekString(4)
                    if (u.length != 4) {
                        throw SyntaxException("File terminated before escaping", section(2, u.length + 2))
                    }
                    buf.append(
                        u.toIntOrNull(16)?.toChar()
                            ?: throw SyntaxException("Illegal unicode escaping", section(2, 6))
                    )
                    nextString(4)
                }
                else -> throw SyntaxException("Unknown escaping", section(2))
            }
        } else if (this.peekString(delim.length) == delim) {
            this.nextString(delim.length)
            eol = true
            break
        } else {
            next()
            buf.append(c)
        }
    }

    if (!eol) {
        throw SyntaxException("Unterminated string", Section(source, absoluteStart, index - absoluteStart))
    }

    processToken(TokenType.STRING, buf.toString(), index - start, index - start, sectionOffset)
}
