package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.*
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
 * A Node Visitor with no parameters and with its interface as return value.
 * NOTE: This file is generated!
 */
public interface NodeMapVisitor {
    public fun visitArrayExpr(node: ArrayExpr): Expr

    public fun visitAssignNode(node: AssignNode): Node

    public fun visitBinaryOperation(node: BinaryOperation): Expr

    public fun visitBooleanExpr(node: BooleanExpr): Expr

    public fun visitBreakExpr(node: BreakExpr): Expr

    public fun visitContinueExpr(node: ContinueExpr): Expr

    public fun visitDecimalExpr(node: DecimalExpr): Expr

    public fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): Expr

    public fun visitDeclareVariableNode(node: DeclareVariableNode): Node

    public fun visitDoWhileNode(node: DoWhileNode): Node

    public fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): Expr

    public fun visitForNode(node: ForNode): Node

    public fun visitFunctionExpr(node: FunctionExpr): Expr

    public fun visitIdentifierExpr(node: IdentifierExpr): Expr

    public fun visitIfExpr(node: IfExpr): Expr

    public fun visitIfNode(node: IfNode): Node

    public fun visitIntegerExpr(node: IntegerExpr): Expr

    public fun visitInvalidNode(node: InvalidNode): Expr

    public fun visitInvokeExpr(node: InvokeExpr): Expr

    public fun visitInvokeLocalExpr(node: InvokeLocalExpr): Expr

    public fun visitInvokeMemberExpr(node: InvokeMemberExpr): Expr

    public fun visitMultiExpr(node: MultiExpr): Expr

    public fun visitMultiNode(node: MultiNode): Node

    public fun visitNullExpr(node: NullExpr): Expr

    public fun visitObjectExpr(node: ObjectExpr): Expr

    public fun visitPropertyAccessExpr(node: PropertyAccessExpr): Expr

    public fun visitPropertyAssignNode(node: PropertyAssignNode): Node

    public fun visitReturnExpr(node: ReturnExpr): Expr

    public fun visitStringExpr(node: StringExpr): Expr

    public fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): Expr

    public fun visitSubscriptAssignNode(node: SubscriptAssignNode): Node

    public fun visitThisExpr(node: ThisExpr): Expr

    public fun visitThrowExpr(node: ThrowExpr): Expr

    public fun visitTryExpr(node: TryExpr): Expr

    public fun visitTypeofExpr(node: TypeofExpr): Expr

    public fun visitUnaryOperation(node: UnaryOperation): Expr

    public fun visitWhileNode(node: WhileNode): Node
}

