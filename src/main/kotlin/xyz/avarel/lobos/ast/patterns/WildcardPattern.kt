package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Section

class WildcardPattern(position: Section) : AbstractPattern("_", position) {
    override fun <R> accept(visitor: PatternVisitor<R>) = visitor.visit(this)
}

