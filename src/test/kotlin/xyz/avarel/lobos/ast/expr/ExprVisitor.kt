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
    fun visit(node: UseExpr): R
    fun visit(node: FileModuleExpr): R
    fun visit(node: FolderModuleExpr): R

    fun visit(node: I32Expr): R
    fun visit(node: I64Expr): R
    fun visit(node: F64Expr): R
    fun visit(node: NullExpr): R

    fun visit(node: InvalidExpr): R
    fun visit(node: StringExpr): R
    fun visit(node: BooleanExpr): R

    fun visit(node: DeclareLetExpr): R
    fun visit(node: DeclareModuleExpr): R
    fun visit(node: DeclareFunctionExpr): R

    fun visit(node: TypeAliasExpr): R
    fun visit(node: AssignExpr): R

    fun visit(node: IdentExpr): R

    fun visit(node: ClosureExpr): R
    fun visit(node: TupleExpr): R
    fun visit(node: ListLiteralExpr): R
    fun visit(node: MapLiteralExpr): R

    fun visit(node: TemplateExpr): R
    fun visit(node: UnaryOperation): R
    fun visit(node: BinaryOperation): R

    fun visit(node: InvokeExpr): R
    fun visit(node: InvokeMemberExpr): R
    fun visit(node: InvokeLocalExpr): R

    fun visit(node: ReturnExpr): R
    fun visit(node: IfExpr): R
    fun visit(node: WhileExpr): R

    fun visit(node: SubscriptAccessExpr): R
    fun visit(node: SubscriptAssignExpr): R
    fun visit(node: PropertyAccessExpr): R
    fun visit(node: PropertyAssignExpr): R
    fun visit(node: TupleIndexAccessExpr): R

    fun visit(node: ExternalModuleExpr): R
    fun visit(node: ExternalLetExpr): R
    fun visit(node: ExternalFunctionExpr): R

    fun visit(node: MultiExpr): R
}