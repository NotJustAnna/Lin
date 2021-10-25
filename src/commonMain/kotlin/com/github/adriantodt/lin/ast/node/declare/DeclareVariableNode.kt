package com.github.adriantodt.lin.ast.node.declare

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

class DeclareVariableNode(
    val name: String, val mutable: Boolean, val value: Expr?, override val section: Section
): Node {
    /* @automation(ast.impl DeclareVariableNode)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitDeclareVariableNode(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitDeclareVariableNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDeclareVariableNode(this, param0)
    /* @automation-end */
}
