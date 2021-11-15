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
 * A Node Visitor with 1 parameter and no return value.
 * NOTE: This file is generated!
 */
public interface NodeVisitor1<T> {
    public fun visitArrayExpr(node: ArrayExpr, param0: T)

    public fun visitAssignNode(node: AssignNode, param0: T)

    public fun visitBinaryOperation(node: BinaryOperation, param0: T)

    public fun visitBooleanExpr(node: BooleanExpr, param0: T)

    public fun visitBreakExpr(node: BreakExpr, param0: T)

    public fun visitContinueExpr(node: ContinueExpr, param0: T)

    public fun visitDecimalExpr(node: DecimalExpr, param0: T)

    public fun visitDeclareFunctionExpr(node: DeclareFunctionExpr, param0: T)

    public fun visitDeclareVariableNode(node: DeclareVariableNode, param0: T)

    public fun visitDoWhileNode(node: DoWhileNode, param0: T)

    public fun visitEnsureNotNullExpr(node: EnsureNotNullExpr, param0: T)

    public fun visitForNode(node: ForNode, param0: T)

    public fun visitFunctionExpr(node: FunctionExpr, param0: T)

    public fun visitIdentifierExpr(node: IdentifierExpr, param0: T)

    public fun visitIfExpr(node: IfExpr, param0: T)

    public fun visitIfNode(node: IfNode, param0: T)

    public fun visitIntegerExpr(node: IntegerExpr, param0: T)

    public fun visitInvalidNode(node: InvalidNode, param0: T)

    public fun visitInvokeExpr(node: InvokeExpr, param0: T)

    public fun visitInvokeLocalExpr(node: InvokeLocalExpr, param0: T)

    public fun visitInvokeMemberExpr(node: InvokeMemberExpr, param0: T)

    public fun visitMultiExpr(node: MultiExpr, param0: T)

    public fun visitMultiNode(node: MultiNode, param0: T)

    public fun visitNullExpr(node: NullExpr, param0: T)

    public fun visitObjectExpr(node: ObjectExpr, param0: T)

    public fun visitPropertyAccessExpr(node: PropertyAccessExpr, param0: T)

    public fun visitPropertyAssignNode(node: PropertyAssignNode, param0: T)

    public fun visitReturnExpr(node: ReturnExpr, param0: T)

    public fun visitStringExpr(node: StringExpr, param0: T)

    public fun visitSubscriptAccessExpr(node: SubscriptAccessExpr, param0: T)

    public fun visitSubscriptAssignNode(node: SubscriptAssignNode, param0: T)

    public fun visitThisExpr(node: ThisExpr, param0: T)

    public fun visitThrowExpr(node: ThrowExpr, param0: T)

    public fun visitTryExpr(node: TryExpr, param0: T)

    public fun visitTypeofExpr(node: TypeofExpr, param0: T)

    public fun visitUnaryOperation(node: UnaryOperation, param0: T)

    public fun visitWhileNode(node: WhileNode, param0: T)
}

