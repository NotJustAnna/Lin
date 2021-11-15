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
public interface NodeVisitor {
    public fun visitArrayExpr(node: ArrayExpr)

    public fun visitAssignNode(node: AssignNode)

    public fun visitBinaryOperation(node: BinaryOperation)

    public fun visitBooleanExpr(node: BooleanExpr)

    public fun visitBreakExpr(node: BreakExpr)

    public fun visitContinueExpr(node: ContinueExpr)

    public fun visitDecimalExpr(node: DecimalExpr)

    public fun visitDeclareFunctionExpr(node: DeclareFunctionExpr)

    public fun visitDeclareVariableNode(node: DeclareVariableNode)

    public fun visitDoWhileNode(node: DoWhileNode)

    public fun visitEnsureNotNullExpr(node: EnsureNotNullExpr)

    public fun visitForNode(node: ForNode)

    public fun visitFunctionExpr(node: FunctionExpr)

    public fun visitIdentifierExpr(node: IdentifierExpr)

    public fun visitIfExpr(node: IfExpr)

    public fun visitIfNode(node: IfNode)

    public fun visitIntegerExpr(node: IntegerExpr)

    public fun visitInvalidNode(node: InvalidNode)

    public fun visitInvokeExpr(node: InvokeExpr)

    public fun visitInvokeLocalExpr(node: InvokeLocalExpr)

    public fun visitInvokeMemberExpr(node: InvokeMemberExpr)

    public fun visitMultiExpr(node: MultiExpr)

    public fun visitMultiNode(node: MultiNode)

    public fun visitNullExpr(node: NullExpr)

    public fun visitObjectExpr(node: ObjectExpr)

    public fun visitPropertyAccessExpr(node: PropertyAccessExpr)

    public fun visitPropertyAssignNode(node: PropertyAssignNode)

    public fun visitReturnExpr(node: ReturnExpr)

    public fun visitStringExpr(node: StringExpr)

    public fun visitSubscriptAccessExpr(node: SubscriptAccessExpr)

    public fun visitSubscriptAssignNode(node: SubscriptAssignNode)

    public fun visitThisExpr(node: ThisExpr)

    public fun visitThrowExpr(node: ThrowExpr)

    public fun visitTryExpr(node: TryExpr)

    public fun visitTypeofExpr(node: TypeofExpr)

    public fun visitUnaryOperation(node: UnaryOperation)

    public fun visitWhileNode(node: WhileNode)
}

