package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

data class DoWhileNode(val body: Node?, val condition: Expr, override val section: Section? = null) : Node {
    /* @automation(ast.impl DoWhileNode,Node)-start */
    override fun accept(visitor: NodeVisitor) = visitor.visitDoWhileNode(this)

    override fun accept(visitor: NodeMapVisitor): Node = visitor.visitDoWhileNode(this)

    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitDoWhileNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoWhileNode(this, param0)
    /* @automation-end */
}
