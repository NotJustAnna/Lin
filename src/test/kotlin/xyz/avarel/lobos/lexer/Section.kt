package xyz.avarel.lobos.lexer

import kotlin.math.max
import kotlin.math.min

data class Section(val source: Source, val lineNumber: Int, val lineIndex: Int, val length: Int) {
    fun span(other: Section): Section {
        return if (source == other.source && lineNumber == other.lineNumber) {
            val min = min(lineIndex, other.lineIndex)
            val max = max(lineIndex + length, other.lineIndex + other.length)
            Section(source, lineNumber, min, (max - min))
        } else {
            this
        }
    }

    fun getLine() = source.lines[lineNumber - 1]

    fun getSubstring() = source.lines[lineNumber - 1].substring(lineIndex, lineIndex + length)

    override fun toString() = "(${source.name}:$lineNumber:$lineIndex)"
}