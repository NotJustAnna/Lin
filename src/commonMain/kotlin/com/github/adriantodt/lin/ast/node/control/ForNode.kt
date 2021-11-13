package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class ForNode(
    val variableName: String,
    val iterable: Expr,
    val body: Node,
    override val section: Section? = null
) : Node {
    /* @automation(ast.impl ForNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitForNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitForNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitForNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitForNode(this, param0)
    /* @automation-end */
}
