package xyz.avarel.lobos.ast.types

import xyz.avarel.lobos.ast.types.basic.IdentTypeAST
import xyz.avarel.lobos.ast.types.basic.NeverTypeAST
import xyz.avarel.lobos.ast.types.basic.NullTypeAST
import xyz.avarel.lobos.ast.types.complex.*

interface TypeASTVisitor<R> {
    fun visit(typeAst: IdentTypeAST): R
    fun visit(typeAst: NeverTypeAST): R
    fun visit(typeAst: NullTypeAST): R
    fun visit(typeAst: FunctionTypeAST): R
    fun visit(typeAst: TupleTypeAST): R
    fun visit(typeAst: UnionTypeAST): R
    fun visit(typeAst: TemplatingTypeAST): R
    fun visit(typeAst: ArrayTypeAST): R
    fun visit(typeAst: MapTypeAst): R
}