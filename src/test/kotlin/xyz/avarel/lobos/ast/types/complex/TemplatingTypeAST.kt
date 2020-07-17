package xyz.avarel.lobos.ast.types.complex

import xyz.avarel.lobos.ast.types.AbstractTypeAST
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.ast.types.TypeASTVisitor
import xyz.avarel.lobos.lexer.Section

class TemplatingTypeAST(val target: TypeAST, val arguments: List<TypeAST>, section: Section) :
    AbstractTypeAST(buildString {
        append(target)
        append('<')
        arguments.joinTo(this)
        append('>')
    }, section) {
    override fun <R> accept(visitor: TypeASTVisitor<R>) = visitor.visit(this)
}

