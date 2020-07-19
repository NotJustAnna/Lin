package io.github.cafeteriaguild.lin.ast.expr

import io.github.cafeteriaguild.lin.ast.expr.access.*
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareFunctionNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareObjectNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareVariableNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.DestructuringVariableNode
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.ast.expr.ops.*

interface NodeVisitor<R> {
    fun visit(expr: NullExpr): R
    fun visit(expr: IntExpr): R
    fun visit(expr: LongExpr): R
    fun visit(expr: FloatExpr): R
    fun visit(expr: DoubleExpr): R
    fun visit(expr: BooleanExpr): R
    fun visit(expr: AssignNode): R
    fun visit(expr: IdentifierExpr): R
    fun visit(expr: DeclareObjectNode): R
    fun visit(expr: DeclareFunctionNode): R
    fun visit(expr: DeclareVariableNode): R
    fun visit(expr: DestructuringVariableNode): R
    fun visit(expr: ReturnExpr): R
    fun visit(expr: ThrowExpr): R
    fun visit(expr: CharExpr): R
    fun visit(expr: StringExpr): R
    fun visit(expr: UnitExpr): R
    fun visit(expr: MultiNode): R
    fun visit(expr: MultiExpr): R
    fun visit(expr: InvalidNode): R
    fun visit(expr: PropertyAccessExpr): R
    fun visit(expr: PropertyAssignNode): R
    fun visit(expr: SubscriptAccessExpr): R
    fun visit(expr: SubscriptAssignNode): R
    fun visit(expr: InvokeExpr): R
    fun visit(expr: InvokeLocalExpr): R
    fun visit(expr: InvokeMemberExpr): R
    fun visit(expr: IfExpr): R
    fun visit(expr: IfNode): R
    fun visit(expr: NotNullExpr): R
    fun visit(expr: DoWhileNode): R
    fun visit(expr: WhileNode): R
    fun visit(expr: ForNode): R
    fun visit(expr: BinaryOperation): R
    fun visit(expr: UnaryOperation): R
    fun visit(expr: PreAssignUnaryOperation): R
    fun visit(expr: PostAssignUnaryOperation): R
    fun visit(expr: AssignOperation): R
    fun visit(expr: ObjectExpr): R
    fun visit(expr: FunctionExpr): R
    fun visit(expr: LambdaExpr): R
}