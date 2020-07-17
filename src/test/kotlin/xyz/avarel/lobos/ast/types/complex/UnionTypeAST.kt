package xyz.avarel.lobos.ast.types.complex

import xyz.avarel.lobos.ast.types.AbstractTypeAST
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.ast.types.TypeASTVisitor
import xyz.avarel.lobos.lexer.Section

class UnionTypeAST(val left: TypeAST, val right: TypeAST, section: Section) :
    AbstractTypeAST("$left | $right", section) {
    override fun <R> accept(visitor: TypeASTVisitor<R>) = visitor.visit(this)
}