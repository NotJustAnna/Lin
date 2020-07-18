package io.github.cafeteriaguild.lin.ast.expr

import io.github.cafeteriaguild.lin.ast.expr.access.*
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareVariableExpr
import io.github.cafeteriaguild.lin.ast.expr.declarations.DestructuringVariableExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeNode
import io.github.cafeteriaguild.lin.ast.expr.misc.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.PostAssignUnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.PreAssignUnaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperation

interface ExprVisitor<R> {
    fun visit(expr: NullNode): R
    fun visit(expr: IntNode): R
    fun visit(expr: LongNode): R
    fun visit(expr: FloatNode): R
    fun visit(expr: DoubleNode): R
    fun visit(expr: BooleanNode): R
    fun visit(expr: AssignExpr): R
    fun visit(expr: IdentifierNode): R
    fun visit(expr: DeclareVariableExpr): R
    fun visit(expr: DestructuringVariableExpr): R
    fun visit(expr: ReturnNode): R
    fun visit(expr: ThrowNode): R
    fun visit(expr: CharNode): R
    fun visit(expr: StringNode): R
    fun visit(expr: UnitNode): R
    fun visit(expr: MultiExpr): R
    fun visit(expr: MultiNode): R
    fun visit(expr: InvalidExpr): R
    fun visit(expr: PropertyAccessNode): R
    fun visit(expr: PropertyAssignExpr): R
    fun visit(expr: SubscriptAccessNode): R
    fun visit(expr: SubscriptAssignExpr): R
    fun visit(expr: InvokeNode): R
    fun visit(expr: InvokeLocalNode): R
    fun visit(expr: InvokeMemberNode): R
    fun visit(expr: IfExpr): R
    fun visit(expr: IfNode): R
    fun visit(expr: NotNullNode): R
    fun visit(expr: DoWhileExpr): R
    fun visit(expr: WhileExpr): R
    fun visit(expr: ForExpr): R
    fun visit(expr: BinaryOperation): R
    fun visit(expr: UnaryOperation): R
    fun visit(expr: PreAssignUnaryOperation): R
    fun visit(expr: PostAssignUnaryOperation): R
    fun visit(expr: FunctionNode): R
    fun visit(expr: DeclareFunctionExpr): R
}