package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Section

abstract class AbstractPattern(val display: String, override val section: Section) : PatternAST {
    override fun toString() = display
}