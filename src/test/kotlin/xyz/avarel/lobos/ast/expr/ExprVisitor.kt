package xyz.avarel.lobos.ast.expr

import xyz.avarel.lobos.ast.expr.access.*
import xyz.avarel.lobos.ast.expr.declarations.*
import xyz.avarel.lobos.ast.expr.files.FileModuleExpr
import xyz.avarel.lobos.ast.expr.files.FolderModuleExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeLocalExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeMemberExpr
import xyz.avarel.lobos.ast.expr.misc.*
import xyz.avarel.lobos.ast.expr.nodes.*
import xyz.avarel.lobos.ast.expr.ops.BinaryOperation
import xyz.avarel.lobos.ast.expr.ops.UnaryOperation

interface ExprVisitor<R> {
    fun visit(expr: UseExpr): R
    fun visit(expr: FileModuleExpr): R
    fun visit(expr: FolderModuleExpr): R

    fun visit(expr: I32Expr): R
    fun visit(expr: I64Expr): R
    fun visit(expr: F64Expr): R
    fun visit(expr: NullExpr): R

    fun visit(expr: InvalidExpr): R
    fun visit(expr: StringExpr): R
    fun visit(expr: BooleanExpr): R

    fun visit(expr: DeclareLetExpr): R
    fun visit(expr: DeclareModuleExpr): R
    fun visit(expr: DeclareFunctionExpr): R

    fun visit(expr: TypeAliasExpr): R
    fun visit(expr: AssignExpr): R

    fun visit(expr: IdentExpr): R

    fun visit(expr: ClosureExpr): R
    fun visit(expr: TupleExpr): R
    fun visit(expr: ListLiteralExpr): R
    fun visit(expr: MapLiteralExpr): R

    fun visit(expr: TemplateExpr): R
    fun visit(expr: UnaryOperation): R
    fun visit(expr: BinaryOperation): R

    fun visit(expr: InvokeExpr): R
    fun visit(expr: InvokeMemberExpr): R
    fun visit(expr: InvokeLocalExpr): R

    fun visit(expr: ReturnExpr): R
    fun visit(expr: IfExpr): R
    fun visit(expr: WhileExpr): R

    fun visit(expr: SubscriptAccessExpr): R
    fun visit(expr: SubscriptAssignExpr): R
    fun visit(expr: PropertyAccessExpr): R
    fun visit(expr: PropertyAssignExpr): R
    fun visit(expr: TupleIndexAccessExpr): R

    fun visit(expr: ExternalModuleExpr): R
    fun visit(expr: ExternalLetExpr): R
    fun visit(expr: ExternalFunctionExpr): R

    fun visit(expr: MultiExpr): R
}