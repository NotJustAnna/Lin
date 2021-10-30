package com.github.adriantodt.lin.compiler

import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.MultiExpr
import com.github.adriantodt.lin.ast.node.MultiNode
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
import com.github.adriantodt.lin.ast.node.control.optimization.LoopNode
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeExpr
import com.github.adriantodt.lin.ast.node.control.optimization.ScopeNode
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.ast.node.invoke.InvokeExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeLocalExpr
import com.github.adriantodt.lin.ast.node.invoke.InvokeMemberExpr
import com.github.adriantodt.lin.ast.node.misc.BinaryOperation
import com.github.adriantodt.lin.ast.node.misc.EnsureNotNullExpr
import com.github.adriantodt.lin.ast.node.misc.TypeofExpr
import com.github.adriantodt.lin.ast.node.misc.UnaryOperation
import com.github.adriantodt.lin.ast.node.value.*
import com.github.adriantodt.lin.ast.visitor.NodeVisitor
import com.github.adriantodt.lin.insn.InsnBuilder

class NodeCompiler : NodeVisitor {
    /*
     * Check out this code:
     * https://github.com/Avarel/Kaiper/blob/master/Kaiper-Compiler/src/main/java/xyz/avarel/kaiper/compiler/ExprCompiler.java
     */
    private val builder = InsnBuilder()

    override fun visitArrayExpr(node: ArrayExpr) {
        builder.makeArrayInsn()
        for (expr in node.value) {
            expr.accept(this)
            builder.arrayInsertInsn()
        }
    }

    override fun visitAssignNode(node: AssignNode) {
        node.value.accept(this)
        builder.assignInsn(node.name)
    }

    override fun visitBinaryOperation(node: BinaryOperation) {
        TODO("Not yet implemented")
    }

    override fun visitBooleanExpr(node: BooleanExpr) {
        builder.pushBooleanInsn(node.value)
    }

    override fun visitBreakExpr(node: BreakExpr) {
        TODO("Not yet implemented")
    }

    override fun visitCharExpr(node: CharExpr) {
        builder.pushCharInsn(node.value)
    }

    override fun visitContinueExpr(node: ContinueExpr) {
        TODO("Not yet implemented")
    }

    override fun visitDeclareFunctionExpr(node: DeclareFunctionExpr) {
        TODO("Not yet implemented")
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode) {
        TODO("Not yet implemented")
    }

    override fun visitDoWhileNode(node: DoWhileNode) {
        TODO("Not yet implemented")
    }

    override fun visitDoubleExpr(node: DoubleExpr) {
        builder.pushDoubleInsn(node.value)
    }

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr) {
        TODO("Not yet implemented")
    }

    override fun visitFloatExpr(node: FloatExpr) {
        builder.pushFloatInsn(node.value)
    }

    override fun visitForNode(node: ForNode) {
        TODO("Not yet implemented")
    }

    override fun visitFunctionExpr(node: FunctionExpr) {
        TODO("Not yet implemented")
    }

    override fun visitIdentifierExpr(node: IdentifierExpr) {
        builder.loadIdentifierInsn(node.name)
    }

    override fun visitIfExpr(node: IfExpr) {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(node: IfNode) {
        TODO("Not yet implemented")
    }

    override fun visitIntExpr(node: IntExpr) {
        builder.pushIntInsn(node.value)
    }

    override fun visitInvalidNode(node: InvalidNode) {
        TODO("Not yet implemented")
    }

    override fun visitInvokeExpr(node: InvokeExpr) {
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeInsn(node.arguments.size)
    }

    override fun visitInvokeLocalExpr(node: InvokeLocalExpr) {
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeLocalInsn(node.name, node.arguments.size)
    }

    override fun visitInvokeMemberExpr(node: InvokeMemberExpr) {
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeMemberInsn(node.name, node.arguments.size)
    }

    override fun visitLongExpr(node: LongExpr) {
        builder.pushLongInsn(node.value)
    }

    override fun visitLoopNode(node: LoopNode) {
        TODO("Not yet implemented")
    }

    override fun visitMultiExpr(node: MultiExpr) {
        TODO("Not yet implemented")
    }

    override fun visitMultiNode(node: MultiNode) {
        TODO("Not yet implemented")
    }

    override fun visitNullExpr(node: NullExpr) {
        TODO("Not yet implemented")
    }

    override fun visitObjectExpr(node: ObjectExpr) {
        TODO("Not yet implemented")
    }

    override fun visitPropertyAccessExpr(node: PropertyAccessExpr) {
        TODO("Not yet implemented")
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode) {
        TODO("Not yet implemented")
    }

    override fun visitReturnExpr(node: ReturnExpr) {
        TODO("Not yet implemented")
    }

    override fun visitScopeExpr(node: ScopeExpr) {
        TODO("Not yet implemented")
    }

    override fun visitScopeNode(node: ScopeNode) {
        TODO("Not yet implemented")
    }

    override fun visitStringExpr(node: StringExpr) {
        builder.pushStringInsn(node.value)
    }

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr) {
        TODO("Not yet implemented")
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode) {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(node: ThisExpr) {
        TODO("Not yet implemented")
    }

    override fun visitThrowExpr(node: ThrowExpr) {
        TODO("Not yet implemented")
    }

    override fun visitTryExpr(node: TryExpr) {
        TODO("Not yet implemented")
    }

    override fun visitTypeofExpr(node: TypeofExpr) {
        TODO("Not yet implemented")
    }

    override fun visitUnaryOperation(node: UnaryOperation) {
        TODO("Not yet implemented")
    }

    override fun visitUnitExpr(node: UnitExpr) {
        TODO("Not yet implemented")
    }

    override fun visitWhileNode(node: WhileNode) {
        TODO("Not yet implemented")
    }
}
