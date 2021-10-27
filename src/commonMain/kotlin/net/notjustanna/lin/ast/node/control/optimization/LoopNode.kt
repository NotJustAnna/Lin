package net.notjustanna.lin.ast.node.control.optimization

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

class LoopNode(val body: Node?, override val section: Section) : Node {
    /* @automation(ast.impl LoopNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitLoopNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitLoopNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitLoopNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitLoopNode(this, param0)
    /* @automation-end */
}
