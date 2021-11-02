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
    private var thisValue: LAny? = null
) : ExecutionLayer {
    private var next: Int = 0

    override fun step(): Boolean {
        val insn = node.instructions.getOrNull(next++)

        if (insn == null) {
            events.onReturn(stack.removeLastOrNull() ?: LNull)
            return false
        }

        when (insn) {
            ArrayInsertInsn -> handleArrayInsert()
            is AssignInsn -> handleAssign(insn.nameConst)
            is BinaryOperationInsn -> handleBinaryOperation(insn.operatorId)
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
            is UnaryOperationInsn -> handleUnaryOperation(insn.operatorId)
        }
        return true
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

    private fun handleBinaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
        if (function !is LFunction) {
            throw IllegalStateException("Can't invoke function for type '${function.linType}'")
        }

        if (function is LFunction.Native) {
            stack.add(function.nativeBlock(arguments))
        }

        if (function !is LFunction.Compiled) {
            throw AssertionError("Impossible.")
        }

        TODO("Not yet implemented")
    }

    private fun handleInvokeLocal(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val function = scope.get(source.stringPool[nameConst])
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

        TODO("Not yet implemented")
    }

    private fun handleInvokeMember(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val parent = stack.removeLast()

        val function = parent.getMember(source.stringPool[nameConst]) ?: LNull
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

        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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

    private fun handleUnaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }
}
