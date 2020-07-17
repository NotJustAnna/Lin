package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Section

class StrPattern(val value: String, position: Section) : AbstractPattern(value, position) {
    override fun <R> accept(visitor: PatternVisitor<R>) = visitor.visit(this)
}