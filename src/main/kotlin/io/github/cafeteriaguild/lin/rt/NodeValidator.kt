package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.ast.node.NodeVisitor
import io.github.cafeteriaguild.lin.ast.node.access.*
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.*
import io.github.cafeteriaguild.lin.ast.node.nodes.*
import io.github.cafeteriaguild.lin.ast.node.ops.*

object NodeValidator : NodeVisitor<Boolean> {
    override fun visit(node: NullExpr) = true

    override fun visit(node: IntExpr) = true

    override fun visit(node: LongExpr) = true

    override fun visit(node: FloatExpr) = true

    override fun visit(node: DoubleExpr) = true

    override fun visit(node: BooleanExpr) = true

    override fun visit(node: AssignNode): Boolean {
        return node.value.accept(this)
    }

    override fun visit(node: IdentifierExpr) = true

    override fun visit(node: DeclareClassNode): Boolean {
        return node.body.all { it.accept(this) }
    }

    override fun visit(node: DeclareEnumClassNode): Boolean {
        return node.body.all { it.accept(this) }
    }

    override fun visit(node: DeclareInterfaceNode): Boolean {
        return node.body.all { it.accept(this) }
    }

    override fun visit(node: DeclareObjectNode): Boolean {
        return node.obj.accept(this)
    }

    override fun visit(node: DeclareFunctionNode): Boolean {
        return node.function.accept(this)
    }

    override fun visit(node: DeclareVariableNode): Boolean {
        return node.value?.accept(this) ?: true
    }

    override fun visit(node: DelegatingVariableNode): Boolean {
        return node.delegate.accept(this)
    }

    override fun visit(node: DestructuringVariableNode): Boolean {
        return node.value.accept(this)
    }

    override fun visit(node: ReturnExpr): Boolean {
        return node.value.accept(this)
    }

    override fun visit(node: ThrowExpr): Boolean {
        return node.value.accept(this)
    }

    override fun visit(node: CharExpr) = true

    override fun visit(node: StringExpr) = true

    override fun visit(node: UnitExpr) = true

    override fun visit(node: MultiNode): Boolean {
        return node.list.all { it.accept(this) }
    }

    override fun visit(node: MultiExpr): Boolean {
        return node.list.all { it.accept(this) } && node.last.accept(this)
    }

    override fun visit(node: InvalidNode) = false

    override fun visit(node: PropertyAccessExpr): Boolean {
        return node.target.accept(this)
    }

    override fun visit(node: PropertyAssignNode): Boolean {
        return node.target.accept(this) && node.value.accept(this)
    }

    override fun visit(node: SubscriptAccessExpr): Boolean {
        return node.target.accept(this) && node.arguments.all { it.accept(this) }
    }

    override fun visit(node: SubscriptAssignNode): Boolean {
        return node.target.accept(this)
                && node.arguments.all { it.accept(this) }
                && node.value.accept(this)
    }

    override fun visit(node: InvokeExpr): Boolean {
        return node.target.accept(this) && node.arguments.all { it.accept(this) }
    }

    override fun visit(node: InvokeLocalExpr): Boolean {
        return node.arguments.all { it.accept(this) }
    }

    override fun visit(node: InvokeMemberExpr): Boolean {
        return node.target.accept(this) && node.arguments.all { it.accept(this) }
    }

    override fun visit(node: IfExpr): Boolean {
        return node.condition.accept(this)
                && node.thenBranch.accept(this)
                && node.elseBranch?.accept(this) ?: true
    }

    override fun visit(node: IfNode): Boolean {
        return node.condition.accept(this)
                && node.thenBranch.accept(this)
                && node.elseBranch.accept(this)
    }

    override fun visit(node: NotNullExpr): Boolean {
        return node.value.accept(this)
    }

    override fun visit(node: DoWhileNode): Boolean {
        return node.body.accept(this) && node.condition.accept(this)
    }

    override fun visit(node: WhileNode): Boolean {
        return node.condition.accept(this) && node.body.accept(this)
    }

    override fun visit(node: ForNode): Boolean {
        return node.iterable.accept(this) && node.body.accept(this)
    }

    override fun visit(node: BreakExpr) = true

    override fun visit(node: ContinueExpr) = true

    override fun visit(node: BinaryOperation): Boolean {
        return node.left.accept(this) && node.right.accept(this)
    }

    override fun visit(node: UnaryOperation): Boolean {
        return node.target.accept(this)
    }

    override fun visit(node: PreAssignUnaryOperation): Boolean {
        return node.target.accept(this)
    }

    override fun visit(node: PostAssignUnaryOperation): Boolean {
        return node.target.accept(this)
    }

    override fun visit(node: AssignOperation): Boolean {
        return node.left.accept(this) && node.right.accept(this)
    }

    override fun visit(node: ObjectExpr): Boolean {
        return node.body.all { it.accept(this) }
    }

    override fun visit(node: FunctionExpr): Boolean {
        return node.parameters.all { it.value?.accept(this) ?: true }
                && node.body?.accept(this) ?: true
    }

    override fun visit(node: LambdaExpr): Boolean {
        return node.body.accept(this)
    }

    override fun visit(node: InitializerNode): Boolean {
        return node.body.accept(this)
    }
}