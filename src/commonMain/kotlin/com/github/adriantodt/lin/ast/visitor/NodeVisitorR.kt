package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
import com.github.adriantodt.lin.ast.node.control.optimization.LoopNode
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.ast.node.invoke.InvokeExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeExtensionExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeLocalExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeMemberExpr
import com.github.adriantodt.lin.ast.node.misc.BinaryOperation
import com.github.adriantodt.lin.ast.node.misc.EnsureNotNullExpr
import com.github.adriantodt.lin.ast.node.misc.TypeofExpr
import com.github.adriantodt.lin.ast.node.misc.UnaryOperation
import com.github.adriantodt.lin.ast.node.value.*

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

    fun visitCharExpr(node: CharExpr): R

    fun visitContinueExpr(node: ContinueExpr): R

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): R

    fun visitDeclareVariableNode(node: DeclareVariableNode): R

    fun visitDoWhileNode(node: DoWhileNode): R

    fun visitDoubleExpr(node: DoubleExpr): R

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): R

    fun visitFloatExpr(node: FloatExpr): R

    fun visitForNode(node: ForNode): R

    fun visitFunctionExpr(node: FunctionExpr): R

    fun visitIdentifierExpr(node: IdentifierExpr): R

    fun visitIfExpr(node: IfExpr): R

    fun visitIfNode(node: IfNode): R

    fun visitIntExpr(node: IntExpr): R

    fun visitInvalidNode(node: InvalidNode): R

    fun visitInvokeExpr(node: InvokeExpr): R

    fun visitInvokeExtensionExpr(node: InvokeExtensionExpr): R

    fun visitInvokeLocalExpr(node: InvokeLocalExpr): R

    fun visitInvokeMemberExpr(node: InvokeMemberExpr): R

    fun visitLongExpr(node: LongExpr): R

    fun visitLoopNode(node: LoopNode): R

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

    fun visitUnitExpr(node: UnitExpr): R

    fun visitWhileNode(node: WhileNode): R
}

