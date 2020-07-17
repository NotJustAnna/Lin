package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Section

class I32Pattern(val value: Int, position: Section) : AbstractPattern(value.toString(), position) {
    override fun <R> accept(visitor: PatternVisitor<R>) = visitor.visit(this)
}