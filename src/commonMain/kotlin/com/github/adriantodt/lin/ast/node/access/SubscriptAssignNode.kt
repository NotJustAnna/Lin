package com.github.adriantodt.lin.ast.node.access

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class SubscriptAssignNode(
    val target: Expr,
    val arguments: List<Expr>,
    val value: Expr,
    override val section: Section? = null
) : Node {
    /* @automation(ast.impl SubscriptAssignNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitSubscriptAssignNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitSubscriptAssignNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitSubscriptAssignNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitSubscriptAssignNode(this, param0)
    }
    /* @automation-end */
}

