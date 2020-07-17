package xyz.avarel.lobos.ast.types

import xyz.avarel.lobos.lexer.Section

abstract class AbstractTypeAST(val name: String, override val section: Section) : TypeAST {
    override fun toString() = name
}