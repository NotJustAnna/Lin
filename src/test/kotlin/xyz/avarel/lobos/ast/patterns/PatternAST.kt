package xyz.avarel.lobos.ast.patterns

import xyz.avarel.lobos.lexer.Sectional

interface PatternAST : Sectional {
    fun <R> accept(visitor: PatternVisitor<R>): R
}
