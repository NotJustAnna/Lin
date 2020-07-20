package io.github.cafeteriaguild.lin.ast.node

import io.github.cafeteriaguild.lin.ast.node.access.*
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.*
import io.github.cafeteriaguild.lin.ast.node.nodes.*
import io.github.cafeteriaguild.lin.ast.node.ops.*

interface NodeParamVisitor<T, R> {
    fun visit(node: NullExpr, param: T): R
    fun visit(node: IntExpr, param: T): R
    fun visit(node: LongExpr, param: T): R
    fun visit(node: FloatExpr, param: T): R
    fun visit(node: DoubleExpr, param: T): R
    fun visit(node: BooleanExpr, param: T): R
    fun visit(node: AssignNode, param: T): R
    fun visit(node: IdentifierExpr, param: T): R
    fun visit(node: DeclareClassNode, param: T): R
    fun visit(node: DeclareEnumClassNode, param: T): R
    fun visit(node: DeclareInterfaceNode, param: T): R
    fun visit(node: DeclareObjectNode, param: T): R
    fun visit(node: DeclareFunctionNode, param: T): R
    fun visit(node: DeclareVariableNode, param: T): R
    fun visit(node: DelegatingVariableNode, param: T): R
    fun visit(node: DestructuringVariableNode, param: T): R
    fun visit(node: ReturnExpr, param: T): R
    fun visit(node: ThrowExpr, param: T): R
    fun visit(node: CharExpr, param: T): R
    fun visit(node: StringExpr, param: T): R
    fun visit(node: UnitExpr, param: T): R
    fun visit(node: MultiNode, param: T): R
    fun visit(node: MultiExpr, param: T): R
    fun visit(node: InvalidNode, param: T): R
    fun visit(node: PropertyAccessExpr, param: T): R
    fun visit(node: PropertyAssignNode, param: T): R
    fun visit(node: SubscriptAccessExpr, param: T): R
    fun visit(node: SubscriptAssignNode, param: T): R
    fun visit(node: InvokeExpr, param: T): R
    fun visit(node: InvokeLocalExpr, param: T): R
    fun visit(node: InvokeMemberExpr, param: T): R
    fun visit(node: IfExpr, param: T): R
    fun visit(node: IfNode, param: T): R
    fun visit(node: NotNullExpr, param: T): R
    fun visit(node: DoWhileNode, param: T): R
    fun visit(node: WhileNode, param: T): R
    fun visit(node: ForNode, param: T): R
    fun visit(node: BreakExpr, param: T): R
    fun visit(node: ContinueExpr, param: T): R
    fun visit(node: BinaryOperation, param: T): R
    fun visit(node: UnaryOperation, param: T): R
    fun visit(node: PreAssignUnaryOperation, param: T): R
    fun visit(node: PostAssignUnaryOperation, param: T): R
    fun visit(node: AssignOperation, param: T): R
    fun visit(node: ObjectExpr, param: T): R
    fun visit(node: FunctionExpr, param: T): R
    fun visit(node: LambdaExpr, param: T): R
    fun visit(node: InitializerNode, param: T): R
}