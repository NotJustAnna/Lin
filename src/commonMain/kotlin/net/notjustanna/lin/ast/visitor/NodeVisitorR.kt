package net.notjustanna.lin.ast.visitor

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

/**
 * A Node Visitor with no parameters and with a parameterized return value.
 * NOTE: This file is generated!
 */
public interface NodeVisitorR<R> {
    public fun visitArrayExpr(node: ArrayExpr): R

    public fun visitAssignNode(node: AssignNode): R

    public fun visitBinaryOperation(node: BinaryOperation): R

    public fun visitBooleanExpr(node: BooleanExpr): R

    public fun visitBreakExpr(node: BreakExpr): R

    public fun visitContinueExpr(node: ContinueExpr): R

    public fun visitDecimalExpr(node: DecimalExpr): R

    public fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): R

    public fun visitDeclareVariableNode(node: DeclareVariableNode): R

    public fun visitDoWhileNode(node: DoWhileNode): R

    public fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): R

    public fun visitForNode(node: ForNode): R

    public fun visitFunctionExpr(node: FunctionExpr): R

    public fun visitIdentifierExpr(node: IdentifierExpr): R

    public fun visitIfExpr(node: IfExpr): R

    public fun visitIfNode(node: IfNode): R

    public fun visitIntegerExpr(node: IntegerExpr): R

    public fun visitInvalidNode(node: InvalidNode): R

    public fun visitInvokeExpr(node: InvokeExpr): R

    public fun visitInvokeLocalExpr(node: InvokeLocalExpr): R

    public fun visitInvokeMemberExpr(node: InvokeMemberExpr): R

    public fun visitMultiExpr(node: MultiExpr): R

    public fun visitMultiNode(node: MultiNode): R

    public fun visitNullExpr(node: NullExpr): R

    public fun visitObjectExpr(node: ObjectExpr): R

    public fun visitPropertyAccessExpr(node: PropertyAccessExpr): R

    public fun visitPropertyAssignNode(node: PropertyAssignNode): R

    public fun visitReturnExpr(node: ReturnExpr): R

    public fun visitStringExpr(node: StringExpr): R

    public fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): R

    public fun visitSubscriptAssignNode(node: SubscriptAssignNode): R

    public fun visitThisExpr(node: ThisExpr): R

    public fun visitThrowExpr(node: ThrowExpr): R

    public fun visitTryExpr(node: TryExpr): R

    public fun visitTypeofExpr(node: TypeofExpr): R

    public fun visitUnaryOperation(node: UnaryOperation): R

    public fun visitWhileNode(node: WhileNode): R
}

