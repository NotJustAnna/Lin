package net.notjustanna.lin.ast.visitor

import net.notjustanna.lin.ast.node.value.ArrayExpr
import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.access.AssignNode
import net.notjustanna.lin.ast.node.Node
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

