package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
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
 * A Node Visitor with no parameters and no return value.
 * NOTE: This file is generated!
 */
interface NodeVisitor0 {
    fun visitTypeofExpr(node: TypeofExpr)

    fun visitUnaryOperation(node: UnaryOperation)

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr)

    fun visitPropertyAccessExpr(node: PropertyAccessExpr)

    fun visitSubscriptAssignNode(node: SubscriptAssignNode)

    fun visitBinaryOperation(node: BinaryOperation)

    fun visitDeclareVariableNode(node: DeclareVariableNode)

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr)

    fun visitAssignNode(node: AssignNode)

    fun visitPropertyAssignNode(node: PropertyAssignNode)

    fun visitIdentifierExpr(node: IdentifierExpr)

    fun visitInvalidNode(node: InvalidNode)

    fun visitMultiExpr(node: MultiExpr)

    fun visitInvokeLocalExpr(node: InvokeLocalExpr)

    fun visitInvokeMemberExpr(node: InvokeMemberExpr)

    fun visitMultiNode(node: MultiNode)

    fun visitInvokeExpr(node: InvokeExpr)

    fun visitBooleanExpr(node: BooleanExpr)

    fun visitIntExpr(node: IntExpr)

    fun visitArrayExpr(node: ArrayExpr)

    fun visitThisExpr(node: ThisExpr)

    fun visitFloatExpr(node: FloatExpr)

    fun visitNullExpr(node: NullExpr)

    fun visitFunctionExpr(node: FunctionExpr)

    fun visitInvokeExtensionExpr(node: InvokeExtensionExpr)

    fun visitLongExpr(node: LongExpr)

    fun visitObjectExpr(node: ObjectExpr)

    fun visitDoubleExpr(node: DoubleExpr)

    fun visitIfExpr(node: IfExpr)

    fun visitTryExpr(node: TryExpr)

    fun visitCharExpr(node: CharExpr)

    fun visitStringExpr(node: StringExpr)

    fun visitIfNode(node: IfNode)

    fun visitReturnExpr(node: ReturnExpr)

    fun visitThrowExpr(node: ThrowExpr)

    fun visitWhileNode(node: WhileNode)

    fun visitForNode(node: ForNode)

    fun visitContinueExpr(node: ContinueExpr)

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr)

    fun visitUnitExpr(node: UnitExpr)

    fun visitDoWhileNode(node: DoWhileNode)

    fun visitBreakExpr(node: BreakExpr)
}
