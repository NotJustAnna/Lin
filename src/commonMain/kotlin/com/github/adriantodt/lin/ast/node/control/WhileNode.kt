package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.ast.visitor.NodeVisitor1
import com.github.adriantodt.lin.ast.visitor.NodeVisitorR
import com.github.adriantodt.tartar.api.lexer.Section

public data class WhileNode(val condition: Expr, val body: Node?, override val section: Section? = null) : Node {
    /* @automation(ast.impl WhileNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitWhileNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitWhileNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitWhileNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitWhileNode(this, param0)
    }
    /* @automation-end */
}
