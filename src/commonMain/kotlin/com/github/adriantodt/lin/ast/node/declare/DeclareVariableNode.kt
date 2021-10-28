package com.github.adriantodt.lin.ast.node.declare

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class DeclareVariableNode(
    val name: String, val mutable: Boolean, val value: Expr?, override val section: Section
) : Node {
    /* @automation(ast.impl DeclareVariableNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitDeclareVariableNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitDeclareVariableNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitDeclareVariableNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDeclareVariableNode(this, param0)
    /* @automation-end */
}
