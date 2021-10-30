package com.github.adriantodt.lin.optimizer

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
import com.github.adriantodt.lin.ast.node.control.optimization.LoopNode
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeExpr
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeNode
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.ast.node.invoke.InvokeExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeLocalExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeMemberExpr
import com.github.adriantodt.lin.ast.node.misc.*
import com.github.adriantodt.lin.ast.node.value.*
import com.github.adriantodt.lin.ast.visitor.NodeMapVisitor

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
        val value = node.value.accept(this) as? FunctionExpr ?: return InvalidNode {
            section(node.section)
            error(RuntimeException("[INTERNAL] NodeOptimizer#visitFunctionExpr did not return FunctionExpr"))
        }
        value.ifInvalidNode()?.let { return it }

        return node.copy(value = value)
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode): Node {
        val value = (node.value ?: return node).accept(this)
        value.ifInvalidNode()?.let { return it }

        return node.copy(value = value)
    }

    override fun visitDoWhileNode(node: DoWhileNode): Node {
        return node // TODO
    }

    override fun visitDoubleExpr(node: DoubleExpr) = node

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): Expr {
        val value = node.value.accept(this)
        value.ifInvalidNode()?.let { return it }

        if (value is NullExpr) {
            // Optimize null nodes into an error.

            val section = node.section
            return ThrowExpr(
                ObjectExpr(
                    listOf(
                        StringExpr("errorType", section) to StringExpr("nullReference", section),
                        StringExpr("section", section) to ObjectExpr(
                            listOf(
                                StringExpr("sourceName", section) to StringExpr(section.source.name, section),
                                StringExpr("index", section) to IntExpr(section.index, section),
                                StringExpr("length", section) to IntExpr(section.length, section)
                            ),
                            section
                        )
                    ),
                    section
                ),
                section
            )
        }

        return node.copy(value = value)
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
        return when (val value = node.condition) {
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
        return when (val value = node.condition) {
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
        val body = node.body?.accept(this) ?: return node
        body.ifInvalidNode()?.let { return it }

        if (body !is Node.Multi) {
            return node.copy(body = body)
        }

        // Unreachable code optimizations are handled at Multi<*>
        val lastNode = body.lastNode()
        if (lastNode is ContinueExpr) {
            return LoopNode(MultiNode(body.nodes().dropLast(1), body.section), node.section)
        }

        // TODO This can't be used as-is, and need some flow detection beforehand i.e there can't be a "continue" anywhere inside the optimized scope.
//        if (lastNode is ReturnExpr) {
//            return ScopeNode(body, node.section) // probably recurse?
//        }
//        if (lastNode is BreakExpr) {
//            return ScopeNode(MultiNode(body.nodes().dropLast(1), body.section), node.section) // probably recurse?
//        }

        return node.copy(body = body)
    }

    override fun visitIntExpr(node: IntExpr) = node

    override fun visitInvalidNode(node: InvalidNode) = node

    override fun visitInvokeExpr(node: InvokeExpr): Expr {
        val target = node.target.accept(this)
        val arguments = node.arguments.map { it.accept(this) }
        (arguments + target).anyInvalidNodes()?.let { return it }
        return node.copy(target = target, arguments = arguments)
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
        val target = node.target.accept(this)
        target.ifInvalidNode()?.let { return it }

        return node.copy(target = target)
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode): Node {
        val target = node.target.accept(this)
        val value = node.value.accept(this)
        listOf(target, value).anyInvalidNodes()?.let { return it }

        return node.copy(target = target, value = value)
    }

    override fun visitReturnExpr(node: ReturnExpr): Expr {
        val value = node.value.accept(this)
        value.ifInvalidNode()?.let { return it }

        return when (value) {
            is ReturnExpr,
            is ThrowExpr,
            is BreakExpr,
            is ContinueExpr -> value
            else -> node.copy(value = value)
        }
    }

    override fun visitScopeExpr(node: ScopeExpr): Expr {
        val body = node.body.accept(this)
        body.ifInvalidNode()?.let { return it }

        return node.copy(body = body)
    }

    override fun visitScopeNode(node: ScopeNode): Node {
        val body = node.body.accept(this)
        body.ifInvalidNode()?.let { return it }

        return node.copy(body = body)
    }

    override fun visitStringExpr(node: StringExpr) = node

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): Expr {
        val target = node.target.accept(this)
        val arguments = node.arguments.map { it.accept(this) }
        (arguments + target).anyInvalidNodes()?.let { return it }

        return node.copy(target = target, arguments = arguments)
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode): Node {
        val target = node.target.accept(this)
        val arguments = node.arguments.map { it.accept(this) }
        val value = node.value.accept(this)
        (arguments + target + value).anyInvalidNodes()?.let { return it }

        return node.copy(target = target, arguments = arguments, value = value)
    }

    override fun visitThisExpr(node: ThisExpr) = node

    override fun visitThrowExpr(node: ThrowExpr): Expr {
        return when (val value = node.value) {
            is ReturnExpr, is ThrowExpr, is BreakExpr, is ContinueExpr -> value
            else -> return node
        }
    }

    override fun visitTryExpr(node: TryExpr): Expr {
        // TODO REDO
        return if (node.catchBranch == null && node.finallyBranch == null) node.tryBranch.exprify() else node
    }

    override fun visitTypeofExpr(node: TypeofExpr): Expr {
        // TODO REDO
        return when (val value = node.value) {
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
        // TODO REDO
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
        return when (val condition = node.condition) {
            is BooleanExpr -> if (condition.value) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is IntExpr -> if (condition.value != 0) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is LongExpr -> if (condition.value != 0L) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is FloatExpr -> if (condition.value != 0f) LoopNode(node.body, node.section) else UnitExpr(node.section)
            is DoubleExpr -> if (condition.value != 0.0) LoopNode(node.body, node.section) else UnitExpr(node.section)
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
