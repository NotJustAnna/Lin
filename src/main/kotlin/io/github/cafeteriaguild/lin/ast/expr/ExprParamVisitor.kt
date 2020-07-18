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

interface ExprParamVisitor<T, R> {
    fun visit(expr: NullNode, param: T): R
    fun visit(expr: IntNode, param: T): R
    fun visit(expr: LongNode, param: T): R
    fun visit(expr: FloatNode, param: T): R
    fun visit(expr: DoubleNode, param: T): R
    fun visit(expr: BooleanNode, param: T): R
    fun visit(expr: AssignExpr, param: T): R
    fun visit(expr: IdentifierNode, param: T): R
    fun visit(expr: DeclareVariableExpr, param: T): R
    fun visit(expr: DestructuringVariableExpr, param: T): R
    fun visit(expr: ReturnNode, param: T): R
    fun visit(expr: ThrowNode, param: T): R
    fun visit(expr: CharNode, param: T): R
    fun visit(expr: StringNode, param: T): R
    fun visit(expr: UnitNode, param: T): R
    fun visit(expr: MultiExpr, param: T): R
    fun visit(expr: MultiNode, param: T): R
    fun visit(expr: InvalidExpr, param: T): R
    fun visit(expr: PropertyAccessNode, param: T): R
    fun visit(expr: PropertyAssignExpr, param: T): R
    fun visit(expr: SubscriptAccessNode, param: T): R
    fun visit(expr: SubscriptAssignExpr, param: T): R
    fun visit(expr: InvokeNode, param: T): R
    fun visit(expr: InvokeLocalNode, param: T): R
    fun visit(expr: InvokeMemberNode, param: T): R
    fun visit(expr: IfExpr, param: T): R
    fun visit(expr: IfNode, param: T): R
    fun visit(expr: NotNullNode, param: T): R
    fun visit(expr: DoWhileExpr, param: T): R
    fun visit(expr: WhileExpr, param: T): R
    fun visit(expr: ForExpr, param: T): R
    fun visit(expr: BinaryOperation, param: T): R
    fun visit(expr: UnaryOperation, param: T): R
    fun visit(expr: PreAssignUnaryOperation, param: T): R
    fun visit(expr: PostAssignUnaryOperation, param: T): R
    fun visit(expr: FunctionNode, param: T): R
    fun visit(expr: DeclareFunctionExpr, param: T): R
}