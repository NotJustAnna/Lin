package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.MutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.*

class DefaultExecutionLayer(
    private val events: VMEvents,
    private var scope: Scope,
    private val source: CompiledSource,
    private val node: CompiledNode = source.nodes[0],
    private val thisValue: LAny? = null
) : ExecutionLayer {
    private var next: Int = 0

    override fun step() {
        val insn = node.instructions.getOrNull(next++)

        if (insn == null) {
            events.onReturn(stack.removeLastOrNull() ?: LNull)
            return
        }

        when (insn) {
            ArrayInsertInsn -> handleArrayInsert()
            is AssignInsn -> handleAssign(insn.nameConst)
            is BranchIfInsn -> handleBranchIf(insn.value, insn.labelCode)
            BreakInsn -> handleBreak()
            CheckNotNullInsn -> handleCheckNotNull()
            ContinueInsn -> handleContinue()
            is DeclareVariableInsn -> handleDeclareVariable(insn.mutable, insn.nameConst)
            DupInsn -> handleDup()
            is GetMemberPropertyInsn -> handleGetMemberProperty(insn.nameConst)
            is GetSubscriptInsn -> handleGetSubscript(insn.size)
            is GetVariableInsn -> handleGetVariable(insn.nameConst)
            is InvokeInsn -> handleInvoke(insn.size)
            is InvokeLocalInsn -> handleInvokeLocal(insn.nameConst, insn.size)
            is InvokeMemberInsn -> handleInvokeMember(insn.nameConst, insn.size)
            is JumpInsn -> handleJump(insn.labelCode)
            is LoadDecimalInsn -> handleLoadDecimal(insn.valueConst)
            is LoadIntegerInsn -> handleLoadInteger(insn.valueConst)
            is LoadStringInsn -> handleLoadString(insn.valueConst)
            NewArrayInsn -> handleNewArray()
            is NewFunctionInsn -> handleNewFunction(insn.functionId)
            NewObjectInsn -> handleNewObject()
            ObjectInsertInsn -> handleObjectInsert()
            PopExceptionHandlingInsn -> handlePopExceptionHandling()
            PopInsn -> handlePop()
            PopLoopHandlingInsn -> handlePopLoopHandling()
            PopScopeInsn -> handlePopScope()
            is PushBooleanInsn -> handlePushBoolean(insn.value)
            is PushDecimalInsn -> handlePushDecimal(insn.immediateValue)
            is PushExceptionHandlingInsn -> handlePushExceptionHandling(insn.catchLabel, insn.endLabel)
            is PushIntegerInsn -> handlePushInteger(insn.immediateValue)
            is PushLoopHandlingInsn -> handlePushLoopHandling(insn.breakLabel, insn.continueLabel)
            PushNullInsn -> handlePushNull()
            PushScopeInsn -> handlePushScope()
            PushThisInsn -> handlePushThis()
            ReturnInsn -> handleReturn()
            is SetMemberPropertyInsn -> handleSetMemberProperty(insn.nameConst)
            is SetSubscriptInsn -> handleSetSubscript(insn.size)
            is SetVariableInsn -> handleSetVariable(insn.nameConst)
            ThrowInsn -> handleThrow()
            TypeofInsn -> handleTypeof()
            BinaryAddOperationInsn -> handleBinaryAddOperation()
            BinaryDivideOperationInsn -> handleBinaryDivideOperation()
            BinaryEqualsOperationInsn -> handleBinaryEqualsOperation()
            BinaryGtOperationInsn -> handleBinaryComparison(Comparison.GT)
            BinaryGteOperationInsn -> handleBinaryComparison(Comparison.GTE)
            BinaryInOperationInsn -> handleBinaryInOperation()
            BinaryLtOperationInsn -> handleBinaryComparison(Comparison.LT)
            BinaryLteOperationInsn -> handleBinaryComparison(Comparison.LTE)
            BinaryMultiplyOperationInsn -> handleBinaryMultiplyOperation()
            BinaryNotEqualsOperationInsn -> handleBinaryNotEqualsOperation()
            BinaryRangeOperationInsn -> handleBinaryRangeOperation()
            BinaryRemainingOperationInsn -> handleBinaryRemainingOperation()
            BinarySubtractOperationInsn -> handleBinarySubtractOperation()
            UnaryNegativeOperationInsn -> handleUnaryNegativeOperation()
            UnaryNotOperationInsn -> handleUnaryNotOperation()
            UnaryPositiveOperationInsn -> handleUnaryPositiveOperation()
            UnaryTruthOperationInsn -> handleUnaryTruthOperation()
        }
    }

    override fun onReturn(value: LAny) {
        stack.add(value)
    }

    override fun onThrow(value: LAny) {
        val handler = exceptionHandlers.removeLastOrNull()
        if (handler == null) {
            events.onThrow(value)
            return
        }
        next = handler.jumpOnException
        stack.add(value)
    }

    data class ExceptionHandler(val keepOnStack: Int, val jumpOnException: Int, val jumpOnEnd: Int)
    data class LoopHandler(val keepOnStack: Int, val jumpOnBreak: Int, val jumpOnContinue: Int)

    private val stack = mutableListOf<LAny>()
    private val exceptionHandlers = mutableListOf<ExceptionHandler>()
    private val loopHandlers = mutableListOf<LoopHandler>()

    private fun handleArrayInsert() {
        val value = stack.removeLast()
        val array = stack.last() as? LArray ?: error("Value is not an LArray.")
        array.value.add(value)
    }

    private fun handleAssign(nameConst: Int) {
        scope.set(source.stringPool[nameConst], stack.removeLast())
    }

    private fun handleBranchIf(value: Boolean, labelCode: Int) {
        val truth = stack.removeLast().truth()

        if (truth == value) {
            next = node.resolveLabel(labelCode)
        }
    }

    private fun handleBreak() {
        val last = loopHandlers.removeLast()
        next = last.jumpOnBreak
    }

    private fun handleCheckNotNull() {
        TODO("Not yet implemented")
    }

    private fun handleContinue() {
        val last = loopHandlers.removeLast()
        next = last.jumpOnContinue
    }

    private fun handleDeclareVariable(mutable: Boolean, nameConst: Int) {
        val s = scope as? MutableScope ?: error("Current scope is not mutable")
        s.declareVariable(source.stringPool[nameConst], mutable)
    }

    private fun handleDup() {
        stack.add(stack.last())
    }

    private fun handleGetMemberProperty(nameConst: Int) {
        val target = stack.removeLast()
        val member = target.getMember(source.stringPool[nameConst]) ?: TODO("Not yet implemented")
        stack.add(member)
    }

    private fun handleGetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleGetVariable(nameConst: Int) {
        stack.add(scope.get(source.stringPool[nameConst]))
    }

    private fun handleInvoke(size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val function = stack.removeLast()
        invocation(null, function, arguments)
    }

    private fun handleInvokeLocal(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val function = scope.get(source.stringPool[nameConst])
        invocation(null, function, arguments)
    }

    private fun handleInvokeMember(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val parent = stack.removeLast()
        val function = parent.getMember(source.stringPool[nameConst]) ?: LNull
        invocation(parent, function, arguments)
    }

    private fun handleJump(labelCode: Int) {
        next = node.resolveLabel(labelCode)
    }

    private fun handleLoadDecimal(valueConst: Int) {
        stack.add(LDecimal(Double.fromBits(source.longPool[valueConst])))
    }

    private fun handleLoadInteger(valueConst: Int) {
        stack.add(LInteger(source.longPool[valueConst]))
    }

    private fun handleLoadString(valueConst: Int) {
        stack.add(LString(source.stringPool[valueConst]))
    }

    private fun handleNewArray() {
        stack.add(LArray())
    }

    private fun handleNewFunction(functionId: Int) {
        val functionData = source.functions[functionId]
        val functionName = if (functionData.nameConst != -1) {
            source.stringPool[functionData.nameConst]
        } else null
        stack.add(LFunction.Compiled(functionName, source, functionData, scope))
    }

    private fun handleNewObject() {
        stack.add(LObject())
    }

    private fun handleObjectInsert() {
        val value = stack.removeLast()
        val key = stack.removeLast()
        val obj = stack.last() as? LObject ?: error("Value is not an LObject.")
        obj.value[key] = value
    }

    private fun handlePopExceptionHandling() {
        exceptionHandlers.removeLast()
    }

    private fun handlePop() {
        stack.removeLast()
    }

    private fun handlePopLoopHandling() {
        loopHandlers.removeLast()
    }

    private fun handlePopScope() {
        scope = scope.parent ?: throw error("Can't pop root scope.")
    }

    private fun handlePushBoolean(value: Boolean) {
        stack.add(if (value) LTrue else LFalse)
    }

    private fun handlePushDecimal(immediateValue: Int) {
        stack.add(LDecimal(immediateValue.toDouble()))
    }

    private fun handlePushExceptionHandling(catchLabel: Int, endLabel: Int) {
        exceptionHandlers.add(ExceptionHandler(stack.size, node.resolveLabel(catchLabel), node.resolveLabel(endLabel)))
    }

    private fun handlePushInteger(immediateValue: Int) {
        stack.add(LInteger(immediateValue.toLong()))
    }

    private fun handlePushLoopHandling(breakLabel: Int, continueLabel: Int) {
        loopHandlers.add(LoopHandler(stack.size, node.resolveLabel(breakLabel), node.resolveLabel(continueLabel)))
    }

    private fun handlePushNull() {
        stack.add(LNull)
    }

    private fun handlePushScope() {
        scope = DefaultMutableScope(scope)
    }

    private fun handlePushThis() {
        stack.add(thisValue ?: error("There's no 'this' defined."))
    }

    private fun handleReturn() {
        events.onReturn(stack.removeLast())
    }

    private fun handleSetMemberProperty(nameConst: Int) {
        val value = stack.removeLast()
        val parent = stack.removeLast() as? LObject ?: TODO("Not yet implemented")
        parent.value[LString(source.stringPool[nameConst])] = value
    }

    private fun handleSetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleSetVariable(nameConst: Int) {
        scope.set(source.stringPool[nameConst], stack.removeLast())
    }

    private fun handleThrow() {
        events.onThrow(stack.removeLast())
    }

    private fun handleTypeof() {
        stack.add(LString(stack.removeLast().linType))
    }

    private fun handleBinaryAddOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LString || right is LString) {
            stack.add(LString(left.toString() + right.toString()))
            return
        }
        if (left is LArray && right is LArray) {
            stack.add(LArray((left.value + right.value).toMutableList()))
            return
        }
        if (left is LNumber && right is LNumber) {
            stack.add(left + right)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleBinaryDivideOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LNumber && right is LNumber) {
            stack.add(left / right)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleBinaryEqualsOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        stack.add(LAny.ofBoolean(right == left))
    }

    private fun handleBinaryMultiplyOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LString && right is LInteger) {
            stack.add(LString(left.value.repeat(right.value.toInt())))
        }
        if (left is LNumber && right is LNumber) {
            stack.add(left * right)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleBinaryNotEqualsOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        stack.add(LAny.ofBoolean(right != left))
    }

    private fun handleBinaryRangeOperation() {
        TODO("Not yet implemented")
    }

    private fun handleBinaryRemainingOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LNumber && right is LNumber) {
            stack.add(left % right)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleBinarySubtractOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LNumber && right is LNumber) {
            stack.add(left - right)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleBinaryComparison(comparison: Comparison) {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (left is LString && right is LString) {
            stack.add(LAny.ofBoolean(comparison.toBoolean(left.value.compareTo(right.value))))
            return
        }
        if (left is LNumber && right is LNumber) {
            stack.add(LAny.ofBoolean(comparison.toBoolean(left.compareTo(right))))
            return
        }
        TODO("Not yet implemented")
    }

    enum class Comparison {
        GT {
            override fun toBoolean(i: Int) = i > 0
        },
        GTE {
            override fun toBoolean(i: Int) = i >= 0
        },
        LT {
            override fun toBoolean(i: Int) = i < 0
        },
        LTE {
            override fun toBoolean(i: Int) = i <= 0
        };

        abstract fun toBoolean(i: Int): Boolean
    }

    private fun handleBinaryInOperation() {
        val right = stack.removeLast()
        val left = stack.removeLast()
        if (right is LArray) {
            stack.add(LAny.ofBoolean(left in right.value))
            return
        }
        if (right is LObject) {
            stack.add(LAny.ofBoolean(left in right.value))
            return
        }

        TODO("Not yet implemented")
    }

    private fun handleUnaryNegativeOperation() {
        val target = stack.removeLast()
        if (target is LNumber) {
            stack.add(-target)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleUnaryNotOperation() {
        stack.add(LAny.ofBoolean(!stack.removeLast().truth()))
    }

    private fun handleUnaryPositiveOperation() {
        val target = stack.removeLast()
        if (target is LNumber) {
            stack.add(+target)
            return
        }
        TODO("Not yet implemented")
    }

    private fun handleUnaryTruthOperation() {
        stack.add(LAny.ofBoolean(stack.removeLast().truth()))
    }

    private fun invocation(thisValue: LAny?, function: LAny, arguments: List<LAny>) {
        if (function !is LFunction) {
            throw IllegalStateException("Can't invoke function for type '${function.linType}'")
        }

        if (function is LFunction.Native) {
            stack.add(function.nativeBlock(arguments))
            return
        }

        if (function !is LFunction.Compiled) {
            throw AssertionError("Impossible.")
        }

        val layer = FunctionSetupLayer(events, function, thisValue, arguments)
        events.pushLayer(layer)
        layer.step()
    }
}
