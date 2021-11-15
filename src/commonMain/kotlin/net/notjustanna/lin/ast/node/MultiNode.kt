package net.notjustanna.lin.ast.node

import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

/**
 * This represents a given list of nodes.
 * Nodes must be executed sequentially.
 * This AST node's main use is function bodies and main scopes.
 */
public data class MultiNode(val list: List<Node>, override val section: Section? = null) : Node.Multi {
    /* @automation(ast.impl MultiNode,Node)-start */
    override fun accept(visitor: NodeVisitor) {
        visitor.visitMultiNode(this)
    }

    override fun accept(visitor: NodeMapVisitor): Node {
        return visitor.visitMultiNode(this)
    }

    override fun <R> accept(visitor: NodeVisitorR<R>): R {
        return visitor.visitMultiNode(this)
    }

    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) {
        visitor.visitMultiNode(this, param0)
    }
    /* @automation-end */

    override fun nodes(): List<Node> {
        return list
    }

    override fun lastNode(): Node = list.last()
}
