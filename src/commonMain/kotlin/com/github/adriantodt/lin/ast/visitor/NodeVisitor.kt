package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.ast.node.invoke.InvokeExpr
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

