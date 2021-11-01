package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.value.ArrayExpr
import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.access.AssignNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.misc.BinaryOperation
import com.github.adriantodt.lin.ast.node.value.BooleanExpr
import com.github.adriantodt.lin.ast.node.control.BreakExpr
import com.github.adriantodt.lin.ast.node.control.ContinueExpr
import com.github.adriantodt.lin.ast.node.value.DecimalExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.ast.node.control.DoWhileNode
import com.github.adriantodt.lin.ast.node.misc.EnsureNotNullExpr
import com.github.adriantodt.lin.ast.node.control.ForNode
import com.github.adriantodt.lin.ast.node.value.FunctionExpr
import com.github.adriantodt.lin.ast.node.access.IdentifierExpr
import com.github.adriantodt.lin.ast.node.control.IfExpr
import com.github.adriantodt.lin.ast.node.control.IfNode
import com.github.adriantodt.lin.ast.node.value.IntegerExpr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.invoke.InvokeExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeLocalExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeMemberExpr
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.value.NullExpr
import com.github.adriantodt.lin.ast.node.value.ObjectExpr
import com.github.adriantodt.lin.ast.node.access.PropertyAccessExpr
import com.github.adriantodt.lin.ast.node.access.PropertyAssignNode
import com.github.adriantodt.lin.ast.node.control.ReturnExpr
import com.github.adriantodt.lin.ast.node.value.StringExpr
import com.github.adriantodt.lin.ast.node.access.SubscriptAccessExpr
import com.github.adriantodt.lin.ast.node.access.SubscriptAssignNode
import com.github.adriantodt.lin.ast.node.value.ThisExpr
import com.github.adriantodt.lin.ast.node.control.ThrowExpr
import com.github.adriantodt.lin.ast.node.control.TryExpr
import com.github.adriantodt.lin.ast.node.misc.TypeofExpr
import com.github.adriantodt.lin.ast.node.misc.UnaryOperation
import com.github.adriantodt.lin.ast.node.control.WhileNode

/**
 * A Node Visitor with no parameters and with its interface as return value.
 * NOTE: This file is generated!
 */
interface NodeMapVisitor {
    fun visitArrayExpr(node: ArrayExpr): Expr

    fun visitAssignNode(node: AssignNode): Node

    fun visitBinaryOperation(node: BinaryOperation): Expr

    fun visitBooleanExpr(node: BooleanExpr): Expr

    fun visitBreakExpr(node: BreakExpr): Expr

    fun visitContinueExpr(node: ContinueExpr): Expr

    fun visitDecimalExpr(node: DecimalExpr): Expr

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): Expr

    fun visitDeclareVariableNode(node: DeclareVariableNode): Node

    fun visitDoWhileNode(node: DoWhileNode): Node

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): Expr

    fun visitForNode(node: ForNode): Node

    fun visitFunctionExpr(node: FunctionExpr): Expr

    fun visitIdentifierExpr(node: IdentifierExpr): Expr

    fun visitIfExpr(node: IfExpr): Expr

    fun visitIfNode(node: IfNode): Node

    fun visitIntegerExpr(node: IntegerExpr): Expr

    fun visitInvalidNode(node: InvalidNode): Expr

    fun visitInvokeExpr(node: InvokeExpr): Expr

    fun visitInvokeLocalExpr(node: InvokeLocalExpr): Expr

    fun visitInvokeMemberExpr(node: InvokeMemberExpr): Expr

    fun visitMultiExpr(node: MultiExpr): Expr

    fun visitMultiNode(node: MultiNode): Node

    fun visitNullExpr(node: NullExpr): Expr

    fun visitObjectExpr(node: ObjectExpr): Expr

    fun visitPropertyAccessExpr(node: PropertyAccessExpr): Expr

    fun visitPropertyAssignNode(node: PropertyAssignNode): Node

    fun visitReturnExpr(node: ReturnExpr): Expr

    fun visitStringExpr(node: StringExpr): Expr

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): Expr

    fun visitSubscriptAssignNode(node: SubscriptAssignNode): Node

    fun visitThisExpr(node: ThisExpr): Expr

    fun visitThrowExpr(node: ThrowExpr): Expr

    fun visitTryExpr(node: TryExpr): Expr

    fun visitTypeofExpr(node: TypeofExpr): Expr

    fun visitUnaryOperation(node: UnaryOperation): Expr

    fun visitWhileNode(node: WhileNode): Node
}

