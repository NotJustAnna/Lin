package io.github.cafeteriaguild.lin.ast.expr

import io.github.cafeteriaguild.lin.ast.expr.access.AssignExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAssignExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.*

interface ExprVisitor<R> {
    fun visit(expr: NullExpr): R
    fun visit(expr: IntExpr): R
    fun visit(expr: LongExpr): R
    fun visit(expr: FloatExpr): R
    fun visit(expr: DoubleExpr): R
    fun visit(expr: BooleanExpr): R
    fun visit(expr: AssignExpr): R
    fun visit(expr: IdentExpr): R
    fun visit(expr: ReturnExpr): R
    fun visit(expr: CharExpr): R
    fun visit(expr: StringExpr): R
    fun visit(expr: UnitExpr): R
    fun visit(expr: MultiExpr): R
    fun visit(expr: InvalidExpr): R
    fun visit(expr: PropertyAccessExpr): R
    fun visit(expr: PropertyAssignExpr): R
    fun visit(expr: InvokeExpr): R
    fun visit(expr: InvokeLocalExpr): R
    fun visit(expr: InvokeMemberExpr): R
}