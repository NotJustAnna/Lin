package io.github.cafeteriaguild.lin.ast.expr

import io.github.cafeteriaguild.lin.ast.expr.access.*
import io.github.cafeteriaguild.lin.ast.expr.declarations.DeclareVariableExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperation
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperation

interface ExprVisitor<R> {
    fun visit(expr: NullExpr): R
    fun visit(expr: IntExpr): R
    fun visit(expr: LongExpr): R
    fun visit(expr: FloatExpr): R
    fun visit(expr: DoubleExpr): R
    fun visit(expr: BooleanExpr): R
    fun visit(expr: AssignExpr): R
    fun visit(expr: IdentifierExpr): R
    fun visit(expr: DeclareVariableExpr): R
    fun visit(expr: ReturnExpr): R
    fun visit(expr: CharExpr): R
    fun visit(expr: StringExpr): R
    fun visit(expr: UnitExpr): R
    fun visit(expr: MultiExpr): R
    fun visit(expr: InvalidExpr): R
    fun visit(expr: PropertyAccessExpr): R
    fun visit(expr: PropertyAssignExpr): R
    fun visit(expr: SubscriptAccessExpr): R
    fun visit(expr: SubscriptAssignExpr): R
    fun visit(expr: InvokeExpr): R
    fun visit(expr: InvokeLocalExpr): R
    fun visit(expr: InvokeMemberExpr): R
    fun visit(expr: IfExpr): R
    fun visit(expr: WhileExpr): R
    fun visit(expr: BinaryOperation): R
    fun visit(expr: UnaryOperation): R
}