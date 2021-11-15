package net.notjustanna.lin.validator

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.MultiExpr
import net.notjustanna.lin.ast.node.MultiNode
import net.notjustanna.lin.ast.node.access.*
import net.notjustanna.lin.ast.node.control.*
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.declare.DeclareVariableNode
import net.notjustanna.lin.ast.node.invoke.InvokeExpr
import net.notjustanna.lin.ast.node.invoke.InvokeLocalExpr
import net.notjustanna.lin.ast.node.invoke.InvokeMemberExpr
import net.notjustanna.lin.ast.node.misc.BinaryOperation
import net.notjustanna.lin.ast.node.misc.EnsureNotNullExpr
import net.notjustanna.lin.ast.node.misc.TypeofExpr
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.ast.node.value.*
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Sectional

public object NodeValidator : NodeVisitorR<InvalidNode?> {
    override fun visitArrayExpr(node: ArrayExpr): InvalidNode? {
        return node.value.mapNotNull { it.accept(this) }.wrap()
    }

    override fun visitAssignNode(node: AssignNode): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitBinaryOperation(node: BinaryOperation): InvalidNode? {
        return listOfNotNull(node.left.accept(this), node.right.accept(this)).wrap()
    }

    override fun visitBooleanExpr(node: BooleanExpr): Nothing? = null

    override fun visitBreakExpr(node: BreakExpr): Nothing? = null

    override fun visitContinueExpr(node: ContinueExpr): Nothing? = null

    override fun visitDecimalExpr(node: DecimalExpr): Nothing? = null

    override fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode): InvalidNode? {
        return node.value?.accept(this)
    }

    override fun visitDoWhileNode(node: DoWhileNode): InvalidNode? {
        return listOfNotNull(
            node.body?.accept(this),
            node.condition.accept(this)
        ).wrap()
    }

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitForNode(node: ForNode): InvalidNode? {
        return listOfNotNull(
            node.iterable.accept(this),
            node.body.accept(this)
        ).wrap()
    }

    override fun visitFunctionExpr(node: FunctionExpr): InvalidNode? {
        return listOfNotNull(
            node.parameters.mapNotNull { it.defaultValue?.accept(this) }.wrap(),
            node.body?.accept(this)
        ).wrap()
    }

    override fun visitIdentifierExpr(node: IdentifierExpr): Nothing? = null

    override fun visitIfExpr(node: IfExpr): InvalidNode? {
        return listOfNotNull(
            node.condition.accept(this),
            node.thenBranch.accept(this),
            node.elseBranch.accept(this)
        ).wrap()
    }

    override fun visitIfNode(node: IfNode): InvalidNode? {
        return listOfNotNull(
            node.condition.accept(this),
            node.thenBranch.accept(this),
            node.elseBranch?.accept(this)
        ).wrap()
    }

    override fun visitIntegerExpr(node: IntegerExpr): Nothing? = null

    override fun visitInvalidNode(node: InvalidNode): InvalidNode {
        if (node.children.isEmpty()) return node

        var optimized = node.children.mapNotNull { it.accept(this) }

        while (optimized.isNotEmpty() && optimized.all { it.errors.isEmpty() }) {
            optimized = optimized.flatMap { it.children.filterIsInstance<InvalidNode>() }
        }

        return node.copy(children = optimized)
    }

    override fun visitInvokeExpr(node: InvokeExpr): InvalidNode? {
        return listOfNotNull(
            node.target.accept(this),
            node.arguments.mapNotNull { it.accept(this) }.wrap()
        ).wrap()
    }

    override fun visitInvokeLocalExpr(node: InvokeLocalExpr): InvalidNode? {
        return node.arguments.mapNotNull { it.accept(this) }.wrap()
    }

    override fun visitInvokeMemberExpr(node: InvokeMemberExpr): InvalidNode? {
        return listOfNotNull(
            node.target.accept(this),
            node.arguments.mapNotNull { it.accept(this) }.wrap()
        ).wrap()
    }

    override fun visitMultiExpr(node: MultiExpr): InvalidNode? {
        return (node.list + node.last).mapNotNull { it.accept(this) }.wrap()
    }

    override fun visitMultiNode(node: MultiNode): InvalidNode? {
        return node.list.mapNotNull { it.accept(this) }.wrap()
    }

    override fun visitNullExpr(node: NullExpr): Nothing? = null

    override fun visitObjectExpr(node: ObjectExpr): InvalidNode? {
        return node.value.flatMap { p -> p.toList().mapNotNull { it.accept(this) } }.wrap()
    }

    override fun visitPropertyAccessExpr(node: PropertyAccessExpr): InvalidNode? {
        return node.target.accept(this)
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode): InvalidNode? {
        return listOfNotNull(
            node.target.accept(this),
            node.value.accept(this),
        ).wrap()
    }

    override fun visitReturnExpr(node: ReturnExpr): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitStringExpr(node: StringExpr): Nothing? = null

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): InvalidNode? {
        return listOfNotNull(
            node.target.accept(this),
            node.arguments.mapNotNull { it.accept(this) }.wrap()
        ).wrap()
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode): InvalidNode? {
        return listOfNotNull(
            node.target.accept(this),
            node.arguments.mapNotNull { it.accept(this) }.wrap(),
            node.value.accept(this)
        ).wrap()
    }

    override fun visitThisExpr(node: ThisExpr): Nothing? = null

    override fun visitThrowExpr(node: ThrowExpr): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitTryExpr(node: TryExpr): InvalidNode? {
        return listOfNotNull(
            node.tryBranch.accept(this),
            node.catchBranch?.branch?.accept(this),
            node.finallyBranch?.accept(this),
        ).wrap()
    }

    override fun visitTypeofExpr(node: TypeofExpr): InvalidNode? {
        return node.value.accept(this)
    }

    override fun visitUnaryOperation(node: UnaryOperation): InvalidNode? {
        return node.target.accept(this)
    }

    override fun visitWhileNode(node: WhileNode): InvalidNode? {
        return listOfNotNull(
            node.condition.accept(this),
            node.body?.accept(this)
        ).wrap()
    }

    private fun List<InvalidNode>.wrap(sectional: Sectional? = null): InvalidNode? {
        return when (size) {
            0 -> null
            1 -> single()
            else -> InvalidNode {
                section(sectional?.section)
                children += this@wrap
            }
        }
    }
}
