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
    fun visitArrayExpr(node: ArrayExpr)

    fun visitAssignNode(node: AssignNode)

    fun visitBinaryOperation(node: BinaryOperation)

    fun visitBooleanExpr(node: BooleanExpr)

    fun visitBreakExpr(node: BreakExpr)

    fun visitCharExpr(node: CharExpr)

    fun visitContinueExpr(node: ContinueExpr)

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr)

    fun visitDeclareVariableNode(node: DeclareVariableNode)

    fun visitDoWhileNode(node: DoWhileNode)

    fun visitDoubleExpr(node: DoubleExpr)

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr)

    fun visitFloatExpr(node: FloatExpr)

    fun visitForNode(node: ForNode)

    fun visitFunctionExpr(node: FunctionExpr)

    fun visitIdentifierExpr(node: IdentifierExpr)

    fun visitIfExpr(node: IfExpr)

    fun visitIfNode(node: IfNode)

    fun visitIntExpr(node: IntExpr)

    fun visitInvalidNode(node: InvalidNode)

    fun visitInvokeExpr(node: InvokeExpr)

    fun visitInvokeExtensionExpr(node: InvokeExtensionExpr)

    fun visitInvokeLocalExpr(node: InvokeLocalExpr)

    fun visitInvokeMemberExpr(node: InvokeMemberExpr)

    fun visitLongExpr(node: LongExpr)

    fun visitMultiExpr(node: MultiExpr)

    fun visitMultiNode(node: MultiNode)

    fun visitNullExpr(node: NullExpr)

    fun visitObjectExpr(node: ObjectExpr)

    fun visitPropertyAccessExpr(node: PropertyAccessExpr)

    fun visitPropertyAssignNode(node: PropertyAssignNode)

    fun visitReturnExpr(node: ReturnExpr)

    fun visitStringExpr(node: StringExpr)

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr)

    fun visitSubscriptAssignNode(node: SubscriptAssignNode)

    fun visitThisExpr(node: ThisExpr)

    fun visitThrowExpr(node: ThrowExpr)

    fun visitTryExpr(node: TryExpr)

    fun visitTypeofExpr(node: TypeofExpr)

    fun visitUnaryOperation(node: UnaryOperation)

    fun visitUnitExpr(node: UnitExpr)

    fun visitWhileNode(node: WhileNode)
}
