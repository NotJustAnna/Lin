package xyz.avarel.lobos.ast.patterns

interface PatternVisitor<R> {
    fun visit(pattern: WildcardPattern): R
    fun visit(pattern: TuplePattern): R
    fun visit(pattern: VariablePattern): R

    fun visit(pattern: I32Pattern): R
    fun visit(pattern: StrPattern): R
}