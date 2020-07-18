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

interface ExprParamVisitor<T, R> {
    fun visit(expr: NullExpr, param: T): R
    fun visit(expr: IntExpr, param: T): R
    fun visit(expr: LongExpr, param: T): R
    fun visit(expr: FloatExpr, param: T): R
    fun visit(expr: DoubleExpr, param: T): R
    fun visit(expr: BooleanExpr, param: T): R
    fun visit(expr: AssignExpr, param: T): R
    fun visit(expr: IdentifierExpr, param: T): R
    fun visit(expr: DeclareVariableExpr, param: T): R
    fun visit(expr: ReturnExpr, param: T): R
    fun visit(expr: CharExpr, param: T): R
    fun visit(expr: StringExpr, param: T): R
    fun visit(expr: UnitExpr, param: T): R
    fun visit(expr: MultiExpr, param: T): R
    fun visit(expr: InvalidExpr, param: T): R
    fun visit(expr: PropertyAccessExpr, param: T): R
    fun visit(expr: PropertyAssignExpr, param: T): R
    fun visit(expr: SubscriptAccessExpr, param: T): R
    fun visit(expr: SubscriptAssignExpr, param: T): R
    fun visit(expr: InvokeExpr, param: T): R
    fun visit(expr: InvokeLocalExpr, param: T): R
    fun visit(expr: InvokeMemberExpr, param: T): R
    fun visit(expr: IfExpr, param: T): R
    fun visit(expr: WhileExpr, param: T): R
    fun visit(expr: BinaryOperation, param: T): R
    fun visit(expr: UnaryOperation, param: T): R
}