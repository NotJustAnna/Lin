package io.github.cafeteriaguild.lin.ast.expr

import io.github.cafeteriaguild.lin.ast.expr.access.*
import io.github.cafeteriaguild.lin.ast.expr.declarations.*
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.ast.expr.ops.*

interface NodeParamVisitor<T, R> {
    fun visit(expr: NullExpr, param: T): R
    fun visit(expr: IntExpr, param: T): R
    fun visit(expr: LongExpr, param: T): R
    fun visit(expr: FloatExpr, param: T): R
    fun visit(expr: DoubleExpr, param: T): R
    fun visit(expr: BooleanExpr, param: T): R
    fun visit(expr: AssignNode, param: T): R
    fun visit(expr: IdentifierExpr, param: T): R
    fun visit(expr: DeclareClassNode, param: T): R
    fun visit(expr: DeclareEnumClassNode, param: T): R
    fun visit(expr: DeclareInterfaceNode, param: T): R
    fun visit(expr: DeclareObjectNode, param: T): R
    fun visit(expr: DeclareFunctionNode, param: T): R
    fun visit(expr: DeclareVariableNode, param: T): R
    fun visit(expr: DelegatingVariableNode, param: T): R
    fun visit(expr: DestructuringVariableNode, param: T): R
    fun visit(expr: ReturnExpr, param: T): R
    fun visit(expr: ThrowExpr, param: T): R
    fun visit(expr: CharExpr, param: T): R
    fun visit(expr: StringExpr, param: T): R
    fun visit(expr: UnitExpr, param: T): R
    fun visit(expr: MultiNode, param: T): R
    fun visit(expr: MultiExpr, param: T): R
    fun visit(expr: InvalidNode, param: T): R
    fun visit(expr: PropertyAccessExpr, param: T): R
    fun visit(expr: PropertyAssignNode, param: T): R
    fun visit(expr: SubscriptAccessExpr, param: T): R
    fun visit(expr: SubscriptAssignNode, param: T): R
    fun visit(expr: InvokeExpr, param: T): R
    fun visit(expr: InvokeLocalExpr, param: T): R
    fun visit(expr: InvokeMemberExpr, param: T): R
    fun visit(expr: IfExpr, param: T): R
    fun visit(expr: IfNode, param: T): R
    fun visit(expr: NotNullExpr, param: T): R
    fun visit(expr: DoWhileNode, param: T): R
    fun visit(expr: WhileNode, param: T): R
    fun visit(expr: ForNode, param: T): R
    fun visit(expr: BinaryOperation, param: T): R
    fun visit(expr: UnaryOperation, param: T): R
    fun visit(expr: PreAssignUnaryOperation, param: T): R
    fun visit(expr: PostAssignUnaryOperation, param: T): R
    fun visit(expr: AssignOperation, param: T): R
    fun visit(expr: ObjectExpr, param: T): R
    fun visit(expr: FunctionExpr, param: T): R
    fun visit(expr: LambdaExpr, param: T): R
    fun visit(expr: InitializerNode, param: T): R
}