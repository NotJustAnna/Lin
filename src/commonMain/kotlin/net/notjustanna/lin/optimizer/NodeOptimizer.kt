package net.notjustanna.lin.optimizer

import net.notjustanna.lin.ast.node.*
import net.notjustanna.lin.ast.node.access.*
import net.notjustanna.lin.ast.node.control.*
import net.notjustanna.lin.ast.node.control.optimization.LoopNode
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.declare.DeclareVariableNode
import net.notjustanna.lin.ast.node.invoke.InvokeExpr
import net.notjustanna.lin.ast.node.invoke.InvokeExtensionExpr
import net.notjustanna.lin.ast.node.invoke.InvokeLocalExpr
import net.notjustanna.lin.ast.node.invoke.InvokeMemberExpr
import net.notjustanna.lin.ast.node.misc.*
import net.notjustanna.lin.ast.node.value.*
import net.notjustanna.lin.ast.visitor.NodeMapVisitor

object NodeOptimizer : NodeMapVisitor {
    override fun visitArrayExpr(node: ArrayExpr): Expr {
        val value = node.value.map { it.accept(this) }
        value.anyInvalidNodes()?.let { return it }

        // Optimization:
        // If there's a explicit return/throw/break/continue, turn the ArrayExpr into a MultiExpr
        // since the array will never be created.
        if (value.any { it is ReturnExpr || it is ThrowExpr || it is BreakExpr || it is ContinueExpr }) {
            val newValue = value.takeWhile {
                it !is ReturnExpr && it !is ThrowExpr && it !is BreakExpr && it !is ContinueExpr
            }

            return MultiExpr(newValue.dropLast(1), newValue.last(), node.section).accept(this)
        }
        return node.copy(value = value)
    }

    override fun visitAssignNode(node: AssignNode): Node {
        val value = node.value.accept(this)
        value.ifInvalidNode()?.let { return it }

        return node.copy(value = value)
    }

    override fun visitBinaryOperation(node: BinaryOperation): Expr {
        val left = node.left.accept(this)
        val right = node.right.accept(this)
        listOf(left, right).anyInvalidNodes()?.let { return it }

        // TODO BinaryOperation optimizations
        // Initially, only at code level
        // In the future it might be worth try to flatten-optimize-rebuild the tree for further optimizations
        return node.copy(left = left, right = right)
    }

    override fun visitBooleanExpr(node: BooleanExpr) = node

    override fun visitBreakExpr(node: BreakExpr) = node

    override fun visitCharExpr(node: CharExpr) = node

    override fun visitContinueExpr(node: ContinueExpr) = node

    override fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): Expr {
        return node
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode): Node {
        return node
    }

    override fun visitDoWhileNode(node: DoWhileNode): Node {
        return node
    }

    override fun visitDoubleExpr(node: DoubleExpr) = node

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): Expr {
        return node
    }

    override fun visitFloatExpr(node: FloatExpr) = node

    override fun visitForNode(node: ForNode): Node {
        return node
    }

    override fun visitFunctionExpr(node: FunctionExpr): Expr {
        return node
    }

    override fun visitIdentifierExpr(node: IdentifierExpr) = node

    override fun visitIfExpr(node: IfExpr): Expr {
        // TODO REDO
        return when(val value = node.condition) {
            is BooleanExpr -> if (value.value) node.thenBranch else node.elseBranch
            is IntExpr -> if (value.value == 0) node.thenBranch else node.elseBranch
            is LongExpr -> if (value.value == 0L) node.thenBranch else node.elseBranch
            is FloatExpr -> if (value.value == 0f) node.thenBranch else node.elseBranch
            is DoubleExpr -> if (value.value == 0.0) node.thenBranch else node.elseBranch
            is NullExpr -> node.elseBranch
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> value
            else -> node
        }
    }

    override fun visitIfNode(node: IfNode): Node {
        // TODO REDO
        return when(val value = node.condition) {
            is BooleanExpr -> if (value.value) node.thenBranch else node.elseBranch ?: UnitExpr(node.section)
            is IntExpr -> if (value.value == 0) node.thenBranch else node.elseBranch ?: UnitExpr(node.section)
            is LongExpr -> if (value.value == 0L) node.thenBranch else node.elseBranch ?: UnitExpr(node.section)
            is FloatExpr -> if (value.value == 0f) node.thenBranch else node.elseBranch ?: UnitExpr(node.section)
            is DoubleExpr -> if (value.value == 0.0) node.thenBranch else node.elseBranch ?: UnitExpr(node.section)
            is NullExpr -> node.elseBranch ?: UnitExpr(node.section)
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> value
            else -> return node
        }
    }

    override fun visitLoopNode(node: LoopNode): Node {
        val body = (node.body ?: return node).accept(this) as Node
        body.ifInvalidNode()?.let { return it }

        if (body !is Node.Multi) {
            return node
        }

        // Unreachable code optimizations are handled at Multi<*>
        val lastNode = body.lastNode()
        if (lastNode is ReturnExpr) {
            return body
        }
        if (lastNode is BreakExpr) {
            return MultiNode(body.nodes().dropLast(1), node.section)
        }

        return node
    }

    override fun visitIntExpr(node: IntExpr) = node

    override fun visitInvalidNode(node: InvalidNode) = node

    override fun visitInvokeExpr(node: InvokeExpr): Expr {
        val target = node.target.accept(this)
        val arguments = node.arguments.map { it.accept(this) }
        (arguments + target).anyInvalidNodes()?.let { return it }
        return node.copy(target = target, arguments = arguments)
    }

    override fun visitInvokeExtensionExpr(node: InvokeExtensionExpr): Expr {
        return node
    }

    override fun visitInvokeLocalExpr(node: InvokeLocalExpr): Expr {
        return node
    }

    override fun visitInvokeMemberExpr(node: InvokeMemberExpr): Expr {
        return node
    }

    override fun visitLongExpr(node: LongExpr) = node

    override fun visitMultiExpr(node: MultiExpr): Expr {
        // TODO Unreachable code optimizations

        // TODO REDO
        val newList = mutableListOf<Node>()
        val dirty = tryOptimizeMulti(node.list, newList)
        return if (dirty) MultiExpr(newList, node.last, node.section) else node
    }

    override fun visitMultiNode(node: MultiNode): Node {
        // TODO Unreachable code optimizations

        // TODO REDO
        val newList = mutableListOf<Node>()
        val dirty = tryOptimizeMulti(node.list, newList)
        return if (dirty) MultiNode(newList, node.section) else node
    }

    override fun visitNullExpr(node: NullExpr) = node

    override fun visitObjectExpr(node: ObjectExpr): Expr {
        // TODO REDO
        val invalidNodes = node.value.unzip().toList().flatten().filterIsInstance<InvalidNode>()
        return when (invalidNodes.size) {
            0 -> node
            1 -> invalidNodes.single()
            else -> InvalidNode {
                section = node.section
                children += invalidNodes
            }
        }
    }

    override fun visitPropertyAccessExpr(node: PropertyAccessExpr): Expr {
        return node
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode): Node {
        return node
    }

    override fun visitReturnExpr(node: ReturnExpr): Expr {
        return when(val value = node.value) {
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> value
            else -> return node
        }
    }

    override fun visitStringExpr(node: StringExpr) = node

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): Expr {
        return node
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode): Node {
        return node
    }

    override fun visitThisExpr(node: ThisExpr) = node

    override fun visitThrowExpr(node: ThrowExpr): Expr {
        return when(val value = node.value) {
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> value
            else -> return node
        }
    }

    override fun visitTryExpr(node: TryExpr): Expr {
        return if (node.catchBranch == null && node.finallyBranch == null) node.tryBranch.exprify() else node
    }

    override fun visitTypeofExpr(node: TypeofExpr): Expr {
        return when(val value = node.value) {
            is BooleanExpr -> StringExpr("boolean", node.span(value))
            is NullExpr -> StringExpr("null", node.span(value))
            is IntExpr -> StringExpr("int", node.span(value))
            is LongExpr -> StringExpr("long", node.span(value))
            is FloatExpr -> StringExpr("float", node.span(value))
            is DoubleExpr -> StringExpr("double", node.span(value))
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr, is InvalidNode -> value
            else -> return node
        }
    }

    override fun visitUnaryOperation(node: UnaryOperation): Expr {
        return when (node.operator) {
            UnaryOperationType.POSITIVE -> when (val target = node.target) {
                is IntExpr -> IntExpr(+target.value, node.span(target))
                is LongExpr -> LongExpr(+target.value, node.span(target))
                is FloatExpr -> FloatExpr(+target.value, node.span(target))
                is DoubleExpr -> DoubleExpr(+target.value, node.span(target))
                is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr, is InvalidNode -> target
                else -> node
            }
            UnaryOperationType.NEGATIVE -> when (val target = node.target) {
                is IntExpr -> IntExpr(-target.value, node.span(target))
                is LongExpr -> LongExpr(-target.value, node.span(target))
                is FloatExpr -> FloatExpr(-target.value, node.span(target))
                is DoubleExpr -> DoubleExpr(-target.value, node.span(target))
                is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr, is InvalidNode -> target
                else -> node
            }
            UnaryOperationType.NOT -> when (val target = node.target) {
                is BooleanExpr -> BooleanExpr(!target.value, node.span(target))
                is IntExpr -> BooleanExpr(target.value == 0, node.span(target))
                is LongExpr -> BooleanExpr(target.value == 0L, node.span(target))
                is FloatExpr -> BooleanExpr(target.value == 0f, node.span(target))
                is DoubleExpr -> BooleanExpr(target.value == 0.0, node.span(target))
                is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr, is InvalidNode -> target
                else -> node
            }
            UnaryOperationType.TRUTH -> when (val target = node.target) {
                is BooleanExpr -> BooleanExpr(target.value, node.span(target))
                is IntExpr -> BooleanExpr(target.value != 0, node.span(target))
                is LongExpr -> BooleanExpr(target.value != 0L, node.span(target))
                is FloatExpr -> BooleanExpr(target.value != 0f, node.span(target))
                is DoubleExpr -> BooleanExpr(target.value != 0.0, node.span(target))
                is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr, is InvalidNode -> target
                else -> node
            }
        }
    }

    override fun visitUnitExpr(node: UnitExpr) = node

    override fun visitWhileNode(node: WhileNode): Node {
        return when(val condition = node.condition) {
            is BooleanExpr -> if(condition.value) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is IntExpr -> if(condition.value != 0) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is LongExpr -> if(condition.value != 0L) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is FloatExpr -> if(condition.value != 0f) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is DoubleExpr -> if(condition.value != 0.0) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> condition
            is InvalidNode -> if (node.body is InvalidNode) InvalidNode {
                section = node.section
                child(node.condition, node.body)
            } else condition
            else -> if (node.body is InvalidNode) node.body else node
        }
    }

    // Utilities

    private fun Node?.ifInvalidNode(): InvalidNode? {
        return this as? InvalidNode
    }

    private fun List<Node>?.anyInvalidNodes(): InvalidNode? {
        if (this == null) return null
        val instances = filterIsInstance<InvalidNode>()
        return when (instances.size) {
            0 -> null
            1 -> instances.single()
            else -> InvalidNode {
                children += instances
                section = children.first().section
            }
        }
    }

    private fun Node.exprify(): Expr {
        if (this is Expr) return this
        if (this is MultiNode) return MultiExpr(list, UnitExpr(section), section)
        return MultiExpr(listOf(this), UnitExpr(section), section)
    }

    private fun tryOptimizeMulti(input: List<Node>, output: MutableList<Node>): Boolean {
        var dirty = false
        for (each in input) {
            val result = optimizeNode(each)
            if (result != null) {
                dirty = true
                output.addAll(result)
                continue
            }
            output.add(each)
        }
        return dirty
    }

    private fun optimizeNode(node: Node): List<Node>? {
        return when (node) {
            is ConstExpr -> emptyList()
            is ArrayExpr -> node.value.flatMap { optimizeNode(it) ?: listOf(it) }
            is ObjectExpr -> node.value.flatMap { it.toList() }
                .flatMap { optimizeNode(it) ?: listOf(it) }
            else -> null
        }
    }
}
