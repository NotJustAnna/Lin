package com.github.adriantodt.lin.ast.visitor

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
import com.github.adriantodt.lin.ast.node.control.optimization.LoopNode
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeExpr
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeNode
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
interface NodeMapVisitor {
    fun visitArrayExpr(node: ArrayExpr): Expr

    fun visitAssignNode(node: AssignNode): Node

    fun visitBinaryOperation(node: BinaryOperation): Expr

    fun visitBooleanExpr(node: BooleanExpr): Expr

    fun visitBreakExpr(node: BreakExpr): Expr

    fun visitCharExpr(node: CharExpr): Expr

    fun visitContinueExpr(node: ContinueExpr): Expr

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr): Expr

    fun visitDeclareVariableNode(node: DeclareVariableNode): Node

    fun visitDoWhileNode(node: DoWhileNode): Node

    fun visitDoubleExpr(node: DoubleExpr): Expr

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr): Expr

    fun visitFloatExpr(node: FloatExpr): Expr

    fun visitForNode(node: ForNode): Node

    fun visitFunctionExpr(node: FunctionExpr): Expr

    fun visitIdentifierExpr(node: IdentifierExpr): Expr

    fun visitIfExpr(node: IfExpr): Expr

    fun visitIfNode(node: IfNode): Node

    fun visitIntExpr(node: IntExpr): Expr

    fun visitInvalidNode(node: InvalidNode): Expr

    fun visitInvokeExpr(node: InvokeExpr): Expr

    fun visitInvokeLocalExpr(node: InvokeLocalExpr): Expr

    fun visitInvokeMemberExpr(node: InvokeMemberExpr): Expr

    fun visitLongExpr(node: LongExpr): Expr

    fun visitLoopNode(node: LoopNode): Node

    fun visitMultiExpr(node: MultiExpr): Expr

    fun visitMultiNode(node: MultiNode): Node

    fun visitNullExpr(node: NullExpr): Expr

    fun visitObjectExpr(node: ObjectExpr): Expr

    fun visitPropertyAccessExpr(node: PropertyAccessExpr): Expr

    fun visitPropertyAssignNode(node: PropertyAssignNode): Node

    fun visitReturnExpr(node: ReturnExpr): Expr

    fun visitScopeExpr(node: ScopeExpr): Expr

    fun visitScopeNode(node: ScopeNode): Node

    fun visitStringExpr(node: StringExpr): Expr

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr): Expr

    fun visitSubscriptAssignNode(node: SubscriptAssignNode): Node

    fun visitThisExpr(node: ThisExpr): Expr

    fun visitThrowExpr(node: ThrowExpr): Expr

    fun visitTryExpr(node: TryExpr): Expr

    fun visitTypeofExpr(node: TypeofExpr): Expr

    fun visitUnaryOperation(node: UnaryOperation): Expr

    fun visitUnitExpr(node: UnitExpr): Expr

    fun visitWhileNode(node: WhileNode): Node
}

