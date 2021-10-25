package net.notjustanna.lin.ast.visitor

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.MultiExpr
import net.notjustanna.lin.ast.node.MultiNode
import net.notjustanna.lin.ast.node.access.*
import net.notjustanna.lin.ast.node.control.*
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.declare.DeclareVariableNode
import net.notjustanna.lin.ast.node.invoke.InvokeExpr
import net.notjustanna.lin.ast.node.invoke.InvokeExtensionExpr
import net.notjustanna.lin.ast.node.invoke.InvokeLocalExpr
import net.notjustanna.lin.ast.node.invoke.InvokeMemberExpr
import net.notjustanna.lin.ast.node.misc.BinaryOperation
import net.notjustanna.lin.ast.node.misc.EnsureNotNullExpr
import net.notjustanna.lin.ast.node.misc.TypeofExpr
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.ast.node.value.*

/**
 * A Node Visitor with no parameters and with a return value.
 * NOTE: This file is generated!
 */
interface NodeVisitor0R<R> {
    fun visitTypeofExpr(node: TypeofExpr): R

    fun visitUnaryOperation(node: UnaryOperation): R

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): R

    fun visitPropertyAccessExpr(node: PropertyAccessExpr): R

    fun visitSubscriptAssignNode(node: SubscriptAssignNode): R

    fun visitBinaryOperation(node: BinaryOperation): R

    fun visitDeclareVariableNode(node: DeclareVariableNode): R

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): R

    fun visitAssignNode(node: AssignNode): R

    fun visitPropertyAssignNode(node: PropertyAssignNode): R

    fun visitIdentifierExpr(node: IdentifierExpr): R

    fun visitInvalidNode(node: InvalidNode): R

    fun visitMultiExpr(node: MultiExpr): R

    fun visitInvokeLocalExpr(node: InvokeLocalExpr): R

    fun visitInvokeMemberExpr(node: InvokeMemberExpr): R

    fun visitMultiNode(node: MultiNode): R

    fun visitInvokeExpr(node: InvokeExpr): R

    fun visitBooleanExpr(node: BooleanExpr): R

    fun visitIntExpr(node: IntExpr): R

    fun visitArrayExpr(node: ArrayExpr): R

    fun visitThisExpr(node: ThisExpr): R

    fun visitFloatExpr(node: FloatExpr): R

    fun visitNullExpr(node: NullExpr): R

    fun visitFunctionExpr(node: FunctionExpr): R

    fun visitInvokeExtensionExpr(node: InvokeExtensionExpr): R

    fun visitLongExpr(node: LongExpr): R

    fun visitObjectExpr(node: ObjectExpr): R

    fun visitDoubleExpr(node: DoubleExpr): R

    fun visitIfExpr(node: IfExpr): R

    fun visitTryExpr(node: TryExpr): R

    fun visitCharExpr(node: CharExpr): R

    fun visitStringExpr(node: StringExpr): R

    fun visitIfNode(node: IfNode): R

    fun visitReturnExpr(node: ReturnExpr): R

    fun visitThrowExpr(node: ThrowExpr): R

    fun visitWhileNode(node: WhileNode): R

    fun visitForNode(node: ForNode): R

    fun visitContinueExpr(node: ContinueExpr): R

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): R

    fun visitUnitExpr(node: UnitExpr): R

    fun visitDoWhileNode(node: DoWhileNode): R

    fun visitBreakExpr(node: BreakExpr): R
}
