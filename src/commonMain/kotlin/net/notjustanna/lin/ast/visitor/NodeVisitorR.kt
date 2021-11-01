package net.notjustanna.lin.ast.visitor

import net.notjustanna.lin.ast.node.value.ArrayExpr
import net.notjustanna.lin.ast.node.access.AssignNode
import net.notjustanna.lin.ast.node.misc.BinaryOperation
import net.notjustanna.lin.ast.node.value.BooleanExpr
import net.notjustanna.lin.ast.node.control.BreakExpr
import net.notjustanna.lin.ast.node.control.ContinueExpr
import net.notjustanna.lin.ast.node.value.DecimalExpr
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.declare.DeclareVariableNode
import net.notjustanna.lin.ast.node.control.DoWhileNode
import net.notjustanna.lin.ast.node.misc.EnsureNotNullExpr
import net.notjustanna.lin.ast.node.control.ForNode
import net.notjustanna.lin.ast.node.value.FunctionExpr
import net.notjustanna.lin.ast.node.access.IdentifierExpr
import net.notjustanna.lin.ast.node.control.IfExpr
import net.notjustanna.lin.ast.node.control.IfNode
import net.notjustanna.lin.ast.node.value.IntegerExpr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.invoke.InvokeExpr
import net.notjustanna.lin.ast.node.invoke.InvokeLocalExpr
import net.notjustanna.lin.ast.node.invoke.InvokeMemberExpr
import net.notjustanna.lin.ast.node.MultiExpr
import net.notjustanna.lin.ast.node.MultiNode
import net.notjustanna.lin.ast.node.value.NullExpr
import net.notjustanna.lin.ast.node.value.ObjectExpr
import net.notjustanna.lin.ast.node.access.PropertyAccessExpr
import net.notjustanna.lin.ast.node.access.PropertyAssignNode
import net.notjustanna.lin.ast.node.control.ReturnExpr
import net.notjustanna.lin.ast.node.value.StringExpr
import net.notjustanna.lin.ast.node.access.SubscriptAccessExpr
import net.notjustanna.lin.ast.node.access.SubscriptAssignNode
import net.notjustanna.lin.ast.node.value.ThisExpr
import net.notjustanna.lin.ast.node.control.ThrowExpr
import net.notjustanna.lin.ast.node.control.TryExpr
import net.notjustanna.lin.ast.node.misc.TypeofExpr
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.ast.node.control.WhileNode

/**
 * A Node Visitor with no parameters and with a parameterized return value.
 * NOTE: This file is generated!
 */
interface NodeVisitorR<R> {
    fun visitArrayExpr(node: ArrayExpr): R

    fun visitAssignNode(node: AssignNode): R

    fun visitBinaryOperation(node: BinaryOperation): R

    fun visitBooleanExpr(node: BooleanExpr): R

    fun visitBreakExpr(node: BreakExpr): R

    fun visitContinueExpr(node: ContinueExpr): R

    fun visitDecimalExpr(node: DecimalExpr): R

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): R

    fun visitDeclareVariableNode(node: DeclareVariableNode): R

    fun visitDoWhileNode(node: DoWhileNode): R

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): R

    fun visitForNode(node: ForNode): R

    fun visitFunctionExpr(node: FunctionExpr): R

    fun visitIdentifierExpr(node: IdentifierExpr): R

    fun visitIfExpr(node: IfExpr): R

    fun visitIfNode(node: IfNode): R

    fun visitIntegerExpr(node: IntegerExpr): R

    fun visitInvalidNode(node: InvalidNode): R

    fun visitInvokeExpr(node: InvokeExpr): R

    fun visitInvokeLocalExpr(node: InvokeLocalExpr): R

    fun visitInvokeMemberExpr(node: InvokeMemberExpr): R

    fun visitMultiExpr(node: MultiExpr): R

    fun visitMultiNode(node: MultiNode): R

    fun visitNullExpr(node: NullExpr): R

    fun visitObjectExpr(node: ObjectExpr): R

    fun visitPropertyAccessExpr(node: PropertyAccessExpr): R

    fun visitPropertyAssignNode(node: PropertyAssignNode): R

    fun visitReturnExpr(node: ReturnExpr): R

    fun visitStringExpr(node: StringExpr): R

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): R

    fun visitSubscriptAssignNode(node: SubscriptAssignNode): R

    fun visitThisExpr(node: ThisExpr): R

    fun visitThrowExpr(node: ThrowExpr): R

    fun visitTryExpr(node: TryExpr): R

    fun visitTypeofExpr(node: TypeofExpr): R

    fun visitUnaryOperation(node: UnaryOperation): R

    fun visitWhileNode(node: WhileNode): R
}

