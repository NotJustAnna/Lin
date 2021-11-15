package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class IfNode(
    val condition: Expr,
    val thenBranch: Node,
    val elseBranch: Node?,
    override val section: Section? = null
) : Node {
    /* @automation(ast.impl IfNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitIfNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitIfNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitIfNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitIfNode(this, param0)
    }
    /* @automation-end */
}
