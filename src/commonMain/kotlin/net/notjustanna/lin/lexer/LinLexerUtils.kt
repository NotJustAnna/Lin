package net.notjustanna.lin.lexer

import net.notjustanna.tartar.api.lexer.LexerContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.extensions.makeToken
import net.notjustanna.tartar.extensions.section

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
    val buf = StringBuilder()
    var eol = false

    while (hasNext()) {
        val c = peek()
        if (c == '$') {
            next()

            if (peek() == '{') {
                next()

                process(makeToken(TokenType.STRING, buf.toString()))
                process(makeToken(TokenType.PLUS))
                buf.clear()

                var braces = 0

                process(makeToken(TokenType.L_PAREN))

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

                process(makeToken(TokenType.R_PAREN))
                process(makeToken(TokenType.PLUS))
            } else if (peek().isLetter()) {
                process(makeToken(TokenType.STRING, buf.toString()))
                process(makeToken(TokenType.PLUS))
                buf.clear()

                buf.append(next())

                while (hasNext() && peek().isLetterOrDigit()) {
                    buf.append(next())
                }

                process(makeToken(TokenType.IDENTIFIER, buf.toString()))
                buf.clear()

                process(makeToken(TokenType.PLUS))
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
                        throw SyntaxException("File terminated before escaping")
                    }
                    buf.append(u.toIntOrNull(16)?.toChar() ?: throw IllegalStateException("Illegal unicode escaping"))
                    nextString(4)
                }
                else -> throw IllegalStateException("Unknown escaping")
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
        throw IllegalStateException("Unterminated string")
    }

    process(makeToken(TokenType.STRING, buf.toString(), 2))
}
