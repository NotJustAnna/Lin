package com.github.adriantodt.lin.compiler

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.ast.node.access.*
import com.github.adriantodt.lin.ast.node.control.*
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
import com.github.adriantodt.lin.bytecode.CompiledParameter
import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.utils.BinaryOperationType
import com.github.adriantodt.tartar.api.parser.SyntaxException

class NodeCompiler(private val source: CompiledSourceBuilder = CompiledSourceBuilder()) : NodeVisitor {
    companion object {
        fun compile(node: Node): CompiledSource {
            val compiler = NodeCompiler()
            node.accept(compiler)
            return compiler.compiledSource()
        }
    }

    private val builder = source.newNodeBuilder()

    fun compiledSource(): CompiledSource {
        return source.build()
    }

    override fun visitArrayExpr(node: ArrayExpr) {
        builder.markSection(node)
        builder.newArrayInsn()
        for (expr in node.value) {
            expr.accept(this)
            builder.arrayInsertInsn()
        }
    }

    override fun visitAssignNode(node: AssignNode) {
        builder.markSection(node)
        node.value.accept(this)
        builder.assignInsn(node.name)
    }

    override fun visitBinaryOperation(node: BinaryOperation) {
        builder.markSection(node)
        node.left.accept(this)
        if (node.operator == BinaryOperationType.ELVIS) {
            val skip = builder.nextLabel()
            builder.dupInsn()
            builder.pushNullInsn()
            builder.binaryOperationInsn(BinaryOperationType.EQUALS)
            builder.branchIfFalseInsn(skip)
            builder.popInsn()
            node.right.accept(this)
            builder.markLabel(skip)
            return
        } else if (node.operator == BinaryOperationType.IS) {
            builder.typeofInsn()
            node.right.accept(this)
            builder.binaryOperationInsn(BinaryOperationType.EQUALS)
            return
        }
        if (node.operator == BinaryOperationType.AND || node.operator == BinaryOperationType.OR) {
            val skip = builder.nextLabel()
            builder.dupInsn()
            if (node.operator == BinaryOperationType.AND) {
                builder.branchIfFalseInsn(skip)
            } else {
                builder.branchIfTrueInsn(skip)
            }
            node.right.accept(this)
            builder.markLabel(skip)
        }
        node.right.accept(this)
        builder.binaryOperationInsn(node.operator)
    }

    override fun visitBooleanExpr(node: BooleanExpr) {
        builder.markSection(node)
        builder.pushBooleanInsn(node.value)
    }

    override fun visitBreakExpr(node: BreakExpr) {
        builder.markSection(node)
        builder.breakInsn()
    }

    override fun visitContinueExpr(node: ContinueExpr) {
        builder.markSection(node)
        builder.continueInsn()
    }

    override fun visitDeclareFunctionExpr(node: DeclareFunctionExpr) {
        builder.markSection(node)
        builder.declareVariableInsn(node.name, false)
        node.accept(this)
        builder.dupInsn()
        builder.setVariableInsn(node.name)
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode) {
        builder.markSection(node)
        builder.declareVariableInsn(node.name, node.mutable)
        if (node.value != null) {
            node.value.accept(this)
            builder.setVariableInsn(node.name)
        }
    }

    override fun visitDoWhileNode(node: DoWhileNode) {
        builder.markSection(node)
        val startLabel = builder.nextLabel()
        builder.markLabel(startLabel)

        if (node.body == null) {
            node.condition.accept(this)
            builder.branchIfTrueInsn(startLabel)
            return
        }

        val conditionLabel = builder.nextLabel()
        val endLabel = builder.nextLabel()

        builder.pushLoopHandlingInsn(conditionLabel, endLabel)

        builder.withScope {
            node.body.accept(this)
        }
        builder.popLoopHandlingInsn()

        builder.markLabel(conditionLabel)
        node.condition.accept(this)
        builder.branchIfTrueInsn(startLabel)
        builder.markLabel(endLabel)
    }

    override fun visitDecimalExpr(node: DecimalExpr) {
        builder.markSection(node)
        builder.pushDecimalInsn(node.value)
    }

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr) {
        builder.markSection(node)
        node.value.accept(this)
        builder.dupInsn()
        builder.invokeLocalInsn("__ensureNotNull", 1)
        builder.popInsn()
    }

    override fun visitForNode(node: ForNode) {
        builder.markSection(node)
        val nextLabel = builder.nextLabel()
        val endLabel = builder.nextLabel()
        builder.withScope {
            val iterator = "${node.variableName}\$__iterator"
            builder.declareVariableInsn(iterator, false)
            node.iterable.accept(this)
            builder.invokeMemberInsn("__iterator", 0)
            builder.setVariableInsn(iterator)

            builder.markLabel(nextLabel)
            builder.getVariableInsn(iterator)
            builder.invokeMemberInsn("__hasNext", 0)
            builder.branchIfFalseInsn(endLabel)
            builder.withScope {
                builder.declareVariableInsn(node.variableName, false)
                builder.getVariableInsn(iterator)
                builder.invokeMemberInsn("__next", 0)
                builder.setVariableInsn(node.variableName)
                node.body.accept(this)
            }
            builder.jumpInsn(nextLabel)
            builder.markLabel(endLabel)
        }
    }

    override fun visitFunctionExpr(node: FunctionExpr) {
        builder.markSection(node)

        val parameters = node.parameters.map { (name, varargs, defaultValue) ->
            CompiledParameter(
                source.constantId(name),
                varargs,
                defaultValue?.let {
                    NodeCompiler(source).also { c -> it.accept(c) }.builder.nodeId
                } ?: -1
            )
        }

        val bodyId = node.body?.let { NodeCompiler(source).also { c -> it.accept(c) }.builder.nodeId } ?: -1

        builder.newFunctionInsn(parameters, node.name, bodyId)
    }

    override fun visitIdentifierExpr(node: IdentifierExpr) {
        builder.markSection(node)
        builder.getVariableInsn(node.name)
    }

    override fun visitIfExpr(node: IfExpr) {
        val elseLabel = builder.nextLabel()
        val endLabel = builder.nextLabel()

        builder.markSection(node)
        node.condition.accept(this)
        builder.branchIfFalseInsn(elseLabel)
        builder.pushScopeInsn()
        node.thenBranch.accept(this)
        builder.popScopeInsn()
        builder.jumpInsn(endLabel)
        builder.markLabel(elseLabel)
        builder.pushScopeInsn()
        node.thenBranch.accept(this)
        builder.popScopeInsn()
        builder.markLabel(endLabel)
    }

    override fun visitIfNode(node: IfNode) {
        builder.markSection(node)

        if (node.elseBranch == null) {
            val endLabel = builder.nextLabel()
            node.condition.accept(this)
            builder.branchIfFalseInsn(endLabel)
            builder.pushScopeInsn()
            node.thenBranch.accept(this)
            builder.popScopeInsn()
            builder.markLabel(endLabel)
        }

        val elseLabel = builder.nextLabel()
        val endLabel = builder.nextLabel()

        builder.markSection(node)
        node.condition.accept(this)
        builder.branchIfFalseInsn(elseLabel)
        builder.pushScopeInsn()
        node.thenBranch.accept(this)
        builder.popScopeInsn()
        builder.jumpInsn(endLabel)
        builder.markLabel(elseLabel)
        builder.pushScopeInsn()
        node.thenBranch.accept(this)
        builder.popScopeInsn()
        builder.markLabel(endLabel)
    }

    override fun visitIntegerExpr(node: IntegerExpr) {
        builder.markSection(node)
        builder.pushIntegerInsn(node.value)
    }

    override fun visitInvalidNode(node: InvalidNode) {
        throw SyntaxException(node.toString(), node.section)
    }

    override fun visitInvokeExpr(node: InvokeExpr) {
        builder.markSection(node)
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeInsn(node.arguments.size)
    }

    override fun visitInvokeLocalExpr(node: InvokeLocalExpr) {
        builder.markSection(node)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeLocalInsn(node.name, node.arguments.size)
    }

    override fun visitInvokeMemberExpr(node: InvokeMemberExpr) {
        builder.markSection(node)
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.invokeMemberInsn(node.name, node.arguments.size)
    }

    override fun visitMultiExpr(node: MultiExpr) {
        builder.markSection(node)
        for (each in node.list) {
            each.accept(this)
            if (each is Expr) {
                builder.popInsn()
            }
        }
        node.last.accept(this)
    }

    override fun visitMultiNode(node: MultiNode) {
        builder.markSection(node)
        for (each in node.list) {
            each.accept(this)
            if (each is Expr) {
                builder.popInsn()
            }
        }
    }

    override fun visitNullExpr(node: NullExpr) {
        builder.markSection(node)
        builder.pushNullInsn()
    }

    override fun visitObjectExpr(node: ObjectExpr) {
        builder.markSection(node)
        builder.newObjectInsn()
        for ((key, value) in node.value) {
            key.accept(this)
            value.accept(this)
            builder.objectInsertInsn()
        }
    }

    override fun visitPropertyAccessExpr(node: PropertyAccessExpr) {
        builder.markSection(node)
        node.target.accept(this)
        if (!node.nullSafe) {
            builder.getMemberPropertyInsn(node.name)
            return
        }

        val skip = builder.nextLabel()
        builder.dupInsn()
        builder.pushNullInsn()
        builder.binaryOperationInsn(BinaryOperationType.EQUALS)
        builder.branchIfTrueInsn(skip)
        builder.getMemberPropertyInsn(node.name)
        builder.markLabel(skip)
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode) {
        builder.markSection(node)
        node.target.accept(this)
        if (!node.nullSafe) {
            node.value.accept(this)
            builder.setMemberPropertyInsn(node.name)
            return
        }

        val skip = builder.nextLabel()
        builder.dupInsn()
        builder.pushNullInsn()
        builder.binaryOperationInsn(BinaryOperationType.EQUALS)
        builder.branchIfTrueInsn(skip)
        node.value.accept(this)
        builder.setMemberPropertyInsn(node.name)
        builder.markLabel(skip)
    }

    override fun visitReturnExpr(node: ReturnExpr) {
        builder.markSection(node)
        node.value.accept(this)
        builder.returnInsn()
    }

    override fun visitStringExpr(node: StringExpr) {
        builder.markSection(node)
        builder.pushStringInsn(node.value)
    }

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr) {
        builder.markSection(node)
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        builder.getSubscriptInsn(node.arguments.size)
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode) {
        builder.markSection(node)
        node.target.accept(this)
        for (argument in node.arguments) {
            argument.accept(this)
        }
        node.value.accept(this)
        builder.setSubscriptInsn(node.arguments.size)
    }

    override fun visitThisExpr(node: ThisExpr) {
        builder.markSection(node)
        builder.pushThisInsn()
    }

    override fun visitThrowExpr(node: ThrowExpr) {
        builder.markSection(node)
        node.value.accept(this)
        builder.throwInsn()
    }

    override fun visitTryExpr(node: TryExpr) {
        // TODO This implementation does not implements `finally`.

        builder.markSection(node)
        if (node.catchBranch == null) {
            builder.pushScopeInsn()
            node.tryBranch.accept(this)
            builder.popScopeInsn()
            return
        }
        val catchLabel = builder.nextLabel()
        val endLabel = builder.nextLabel()

        builder.withExceptionHandling(catchLabel, endLabel) {
            builder.withScope {
                node.tryBranch.accept(this)
            }
        }

        builder.jumpInsn(endLabel)
        builder.markLabel(catchLabel)
        if (node.catchBranch.caughtName == null) {
            builder.popInsn()
        } else {
            builder.pushScopeInsn()
            builder.declareVariableInsn(node.catchBranch.caughtName, false)
            builder.setVariableInsn(node.catchBranch.caughtName)
        }

        builder.withScope {
            node.catchBranch.branch.accept(this)
        }

        if (node.catchBranch.caughtName != null) {
            builder.popScopeInsn()
        }
        builder.markLabel(endLabel)
    }

    override fun visitTypeofExpr(node: TypeofExpr) {
        builder.markSection(node)
        node.value.accept(this)
        builder.typeofInsn()
    }

    override fun visitUnaryOperation(node: UnaryOperation) {
        builder.markSection(node)
        node.target.accept(this)
        builder.unaryOperationInsn(node.operator)
    }

    override fun visitWhileNode(node: WhileNode) {
        builder.markSection(node)
        val startLabel = builder.nextLabel()
        builder.markLabel(startLabel)
        node.condition.accept(this)

        if (node.body == null) {
            builder.branchIfTrueInsn(startLabel)
            return
        }

        val endLabel = builder.nextLabel()
        builder.branchIfFalseInsn(endLabel)

        builder.withLoopHandling(startLabel, endLabel) {
            builder.withScope {
                node.body.accept(this)
            }
        }
        builder.jumpInsn(startLabel)
        builder.markLabel(endLabel)
    }
}
