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
 * A Node Visitor with no parameters and no return value.
 * NOTE: This file is generated!
 */
interface NodeVisitor {
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

    fun visitWhileNode(node: WhileNode)
}

