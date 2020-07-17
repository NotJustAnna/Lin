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

interface ExprParamVisitor<T, R> {
    fun visit(expr: NullExpr, param: T): R
    fun visit(expr: IntExpr, param: T): R
    fun visit(expr: LongExpr, param: T): R
    fun visit(expr: FloatExpr, param: T): R
    fun visit(expr: DoubleExpr, param: T): R
    fun visit(expr: BooleanExpr, param: T): R
    fun visit(expr: AssignExpr, param: T): R
    fun visit(expr: IdentExpr, param: T): R
    fun visit(expr: ReturnExpr, param: T): R
    fun visit(expr: CharExpr, param: T): R
    fun visit(expr: StringExpr, param: T): R
    fun visit(expr: UnitExpr, param: T): R
    fun visit(expr: MultiExpr, param: T): R
    fun visit(expr: InvalidExpr, param: T): R
    fun visit(expr: PropertyAccessExpr, param: T): R
    fun visit(expr: PropertyAssignExpr, param: T): R
    fun visit(expr: InvokeExpr, param: T): R
    fun visit(expr: InvokeLocalExpr, param: T): R
    fun visit(expr: InvokeMemberExpr, param: T): R
}