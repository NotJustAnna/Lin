package net.notjustanna.lin.ast.visitor

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.MultiExpr
import net.notjustanna.lin.ast.node.MultiNode
import net.notjustanna.lin.ast.node.access.*
import net.notjustanna.lin.ast.node.control.*
import net.notjustanna.lin.ast.node.control.optimization.LoopNode
import net.notjustanna.lin.ast.node.control.optimization.ScopeExpr
import net.notjustanna.lin.ast.node.control.optimization.ScopeNode
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
 * A Node Visitor with 1 parameter and no return value.
 * NOTE: This file is generated!
 */
interface NodeVisitor1<T> {
    fun visitArrayExpr(node: ArrayExpr, param0: T)

    fun visitAssignNode(node: AssignNode, param0: T)

    fun visitBinaryOperation(node: BinaryOperation, param0: T)

    fun visitBooleanExpr(node: BooleanExpr, param0: T)

    fun visitBreakExpr(node: BreakExpr, param0: T)

    fun visitCharExpr(node: CharExpr, param0: T)

    fun visitContinueExpr(node: ContinueExpr, param0: T)

    fun visitDeclareFunctionExpr(node: DeclareFunctionExpr, param0: T)

    fun visitDeclareVariableNode(node: DeclareVariableNode, param0: T)

    fun visitDoWhileNode(node: DoWhileNode, param0: T)

    fun visitDoubleExpr(node: DoubleExpr, param0: T)

    fun visitEnsureNotNullExpr(node: EnsureNotNullExpr, param0: T)

    fun visitFloatExpr(node: FloatExpr, param0: T)

    fun visitForNode(node: ForNode, param0: T)

    fun visitFunctionExpr(node: FunctionExpr, param0: T)

    fun visitIdentifierExpr(node: IdentifierExpr, param0: T)

    fun visitIfExpr(node: IfExpr, param0: T)

    fun visitIfNode(node: IfNode, param0: T)

    fun visitIntExpr(node: IntExpr, param0: T)

    fun visitInvalidNode(node: InvalidNode, param0: T)

    fun visitInvokeExpr(node: InvokeExpr, param0: T)

    fun visitInvokeLocalExpr(node: InvokeLocalExpr, param0: T)

    fun visitInvokeMemberExpr(node: InvokeMemberExpr, param0: T)

    fun visitLongExpr(node: LongExpr, param0: T)

    fun visitLoopNode(node: LoopNode, param0: T)

    fun visitMultiExpr(node: MultiExpr, param0: T)

    fun visitMultiNode(node: MultiNode, param0: T)

    fun visitNullExpr(node: NullExpr, param0: T)

    fun visitObjectExpr(node: ObjectExpr, param0: T)

    fun visitPropertyAccessExpr(node: PropertyAccessExpr, param0: T)

    fun visitPropertyAssignNode(node: PropertyAssignNode, param0: T)

    fun visitReturnExpr(node: ReturnExpr, param0: T)

    fun visitScopeExpr(node: ScopeExpr, param0: T)

    fun visitScopeNode(node: ScopeNode, param0: T)

    fun visitStringExpr(node: StringExpr, param0: T)

    fun visitSubscriptAccessExpr(node: SubscriptAccessExpr, param0: T)

    fun visitSubscriptAssignNode(node: SubscriptAssignNode, param0: T)

    fun visitThisExpr(node: ThisExpr, param0: T)

    fun visitThrowExpr(node: ThrowExpr, param0: T)

    fun visitTryExpr(node: TryExpr, param0: T)

    fun visitTypeofExpr(node: TypeofExpr, param0: T)

    fun visitUnaryOperation(node: UnaryOperation, param0: T)

    fun visitUnitExpr(node: UnitExpr, param0: T)

    fun visitWhileNode(node: WhileNode, param0: T)
}

