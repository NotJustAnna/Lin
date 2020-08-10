package io.github.cafeteriaguild.lin.ast.node.declarations

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.*

class DeclareEnumClassNode(
    val name: String,
    //val parameters: List<DeclareClassNode.Parameter>,
    //val implements: List<AccessExpr>,
    val values: List<String>,
    val body: List<Declaration>,
    section: Section,
    val modifiers: Set<LinModifier> = emptySet()
) : AbstractNode(section), ObjectDeclaration {
    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}