package xyz.avarel.lobos.ast.types

import xyz.avarel.lobos.lexer.Sectional

interface TypeAST : Sectional {
    fun <R> accept(visitor: TypeASTVisitor<R>): R
}