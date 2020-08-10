package io.github.cafeteriaguild.lin.ast.node.declarations

import net.notjustanna.tartar.api.lexer.Section
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.*

class DeclareClassNode(
    val name: String,
    val parameters: List<Parameter>,
    val implements: List<AccessExpr>,
    val body: List<Declaration>,
    section: Section,
    val modifiers: Set<LinModifier> = emptySet()
) : AbstractNode(section), ObjectDeclaration {
    data class Parameter(
        val name: String,
        val varargs: Boolean,
        val type: ParameterType,
        val value: Expr?
    )

    enum class ParameterType {
        ARGUMENT, VAL, VAR
    }

    override fun <R> accept(visitor: NodeVisitor<R>) = visitor.visit(this)
    override fun <T, R> accept(visitor: NodeParamVisitor<T, R>, param: T) = visitor.visit(this, param)
}