package io.github.cafeteriaguild.lin.ast.node

import io.github.cafeteriaguild.lin.ast.node.access.*
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.*
import io.github.cafeteriaguild.lin.ast.node.nodes.*
import io.github.cafeteriaguild.lin.ast.node.ops.*

interface NodeVisitor<R> {
    fun visit(node: NullExpr): R
    fun visit(node: IntExpr): R
    fun visit(node: LongExpr): R
    fun visit(node: FloatExpr): R
    fun visit(node: DoubleExpr): R
    fun visit(node: BooleanExpr): R
    fun visit(node: AssignNode): R
    fun visit(node: IdentifierExpr): R
    fun visit(node: DeclareClassNode): R
    fun visit(node: DeclareEnumClassNode): R
    fun visit(node: DeclareInterfaceNode): R
    fun visit(node: DeclareObjectNode): R
    fun visit(node: DeclareFunctionNode): R
    fun visit(node: DeclareVariableNode): R
    fun visit(node: DelegatingVariableNode): R
    fun visit(node: DestructuringVariableNode): R
    fun visit(node: ReturnExpr): R
    fun visit(node: ThrowExpr): R
    fun visit(node: CharExpr): R
    fun visit(node: StringExpr): R
    fun visit(node: UnitExpr): R
    fun visit(node: MultiNode): R
    fun visit(node: MultiExpr): R
    fun visit(node: InvalidNode): R
    fun visit(node: PropertyAccessExpr): R
    fun visit(node: PropertyAssignNode): R
    fun visit(node: SubscriptAccessExpr): R
    fun visit(node: SubscriptAssignNode): R
    fun visit(node: InvokeExpr): R
    fun visit(node: InvokeLocalExpr): R
    fun visit(node: InvokeMemberExpr): R
    fun visit(node: IfExpr): R
    fun visit(node: IfNode): R
    fun visit(node: NotNullExpr): R
    fun visit(node: DoWhileNode): R
    fun visit(node: WhileNode): R
    fun visit(node: ForNode): R
    fun visit(node: BreakExpr): R
    fun visit(node: ContinueExpr): R
    fun visit(node: BinaryOperation): R
    fun visit(node: UnaryOperation): R
    fun visit(node: PreAssignUnaryOperation): R
    fun visit(node: PostAssignUnaryOperation): R
    fun visit(node: AssignOperation): R
    fun visit(node: ObjectExpr): R
    fun visit(node: FunctionExpr): R
    fun visit(node: LambdaExpr): R
    fun visit(node: InitializerNode): R
}