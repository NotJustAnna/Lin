package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Section

class TuplePattern(val list: List<PatternAST>, position: Section) :
    AbstractPattern(list.joinToString(prefix = "(", postfix = ")"), position) {
    override fun <R> accept(visitor: PatternVisitor<R>) = visitor.visit(this)
}