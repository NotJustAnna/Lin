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
 * A Node Visitor with no parameters and no return value.
 * NOTE: This file is generated!
 */
interface NodeVisitor {
    fun visitArrayExpr(node: ArrayExpr)

    fun visitAssignNode(node: AssignNode)

    fun visitBinaryOperation(node: BinaryOperation)

    fun visitBooleanExpr(node: BooleanExpr)

    fun visitBreakExpr(node: BreakExpr)

    fun visitContinueExpr(node: ContinueExpr)

    fun visitDecimalExpr(node: DecimalExpr)

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr)

    fun visitDeclareVariableNode(node: DeclareVariableNode)

    fun visitDoWhileNode(node: DoWhileNode)

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr)

    fun visitForNode(node: ForNode)

    fun visitFunctionExpr(node: FunctionExpr)

    fun visitIdentifierExpr(node: IdentifierExpr)

    fun visitIfExpr(node: IfExpr)

    fun visitIfNode(node: IfNode)

    fun visitIntegerExpr(node: IntegerExpr)

    fun visitInvalidNode(node: InvalidNode)

    fun visitInvokeExpr(node: InvokeExpr)

    fun visitInvokeLocalExpr(node: InvokeLocalExpr)

    fun visitInvokeMemberExpr(node: InvokeMemberExpr)

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

