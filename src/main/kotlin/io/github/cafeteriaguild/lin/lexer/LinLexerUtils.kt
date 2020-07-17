package io.github.cafeteriaguild.lin.lexer

import net.notjustanna.tartar.api.lexer.LexerContext

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
            when (next()) {
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
                else -> throw IllegalStateException("Unknown escaping")
            }
        } else {
            next()
            buf.append(c)
        }
    }
    if (!eol) {
        throw IllegalStateException("Unterminated lin string")
    }
    return buf.toString()
}
