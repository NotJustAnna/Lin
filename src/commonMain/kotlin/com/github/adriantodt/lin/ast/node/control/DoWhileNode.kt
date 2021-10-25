package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0
import com.github.adriantodt.lin.ast.visitor.NodeVisitor0R
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.tartar.api.lexer.Section

data class DoWhileNode(val body: Node?, val condition: Expr, override val section: Section) : Node {
    /* @automation(ast.impl DoWhileNode)-start */
    override fun accept(visitor: NodeVisitor0) = visitor.visitDoWhileNode(this)

    override fun <R> accept(visitor: NodeVisitor0R<R>): R = visitor.visitDoWhileNode(this)

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitDoWhileNode(this, param0)
    /* @automation-end */
}
