package com.github.adriantodt.lin.ast.node.control.optimization

import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

class LoopNode(val body: Node?, override val section: Section) : Node {
    /* @automation(ast.impl LoopNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitLoopNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitLoopNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitLoopNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitLoopNode(this, param0)
    /* @automation-end */
}
