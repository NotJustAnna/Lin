package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.MutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.*

interface InsnEvents {
    fun jumpToLabel(labelCode: Int)
}

class InsnHandler(var currentScope: Scope, var currentSource: CompiledSource, private val events: InsnEvents) {

    var currentThis: LAny? = null
    var stack = mutableListOf<LAny>()
    var exceptionHandlers = mutableListOf<ExceptionHandler>()
    var loopHandlers = mutableListOf<LoopHandler>()

    fun handleArrayInsert() {
        val value = stack.removeLast()
        val array = stack.last() as? LArray ?: error("Value is not an LArray.")
        array.value.add(value)
    }

    fun handleAssign(nameConst: Int) {
        currentScope.set(currentSource.stringPool[nameConst], stack.removeLast())
    }

    fun handleBinaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }

    fun handleBranchIf(value: Boolean, labelCode: Int) {
        val truth = stack.removeLast().truth()

        if (truth == value) {
            events.jumpToLabel(labelCode)
        }
    }

    fun handleBreak() {
        TODO("Not yet implemented")
    }

    fun handleCheckNotNull() {
        TODO("Not yet implemented")
    }

    fun handleContinue() {
        TODO("Not yet implemented")
    }

    fun handleDeclareVariable(mutable: Boolean, nameConst: Int) {
        val s = currentScope as? MutableScope ?: error("Current scope is not mutable")
        s.declareVariable(currentSource.stringPool[nameConst], mutable)
    }

    fun handleDup() {
        stack.add(stack.last())
    }

    fun handleGetMemberProperty(nameConst: Int) {
        TODO("Not yet implemented")
    }

    fun handleGetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    fun handleGetVariable(nameConst: Int) {
        stack.add(currentScope.get(currentSource.stringPool[nameConst]))
    }

    fun handleInvoke(size: Int) {
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

    fun handleInvokeLocal(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val function = currentScope.get(currentSource.stringPool[nameConst])
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

    fun handleInvokeMember(nameConst: Int, size: Int) {
        val arguments = List(size) { stack.removeLast() }.reversed()
        val parent = stack.removeLast()

        val function = parent.getMember(currentSource.stringPool[nameConst]) ?: LNull
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

    fun handleJump(labelCode: Int) {
        events.jumpToLabel(labelCode)
    }

    fun handleLoadDecimal(valueConst: Int) {
        stack.add(LDecimal(Double.fromBits(currentSource.longPool[valueConst])))
    }

    fun handleLoadInteger(valueConst: Int) {
        stack.add(LInteger(currentSource.longPool[valueConst]))
    }

    fun handleLoadString(valueConst: Int) {
        stack.add(LString(currentSource.stringPool[valueConst]))
    }

    fun handleNewArray() {
        stack.add(LArray())
    }

    fun handleNewFunction(functionId: Int) {
        val functionData = currentSource.functions[functionId]
        val functionName = if (functionData.nameConst != -1) {
            currentSource.stringPool[functionData.nameConst]
        } else null
        stack.add(LFunction.Compiled(functionName, currentSource, functionData, currentScope))
    }

    fun handleNewObject() {
        stack.add(LObject())
    }

    fun handleObjectInsert() {
        val value = stack.removeLast()
        val key = stack.removeLast()
        val obj = stack.last() as? LObject ?: error("Value is not an LObject.")
        obj.value[key] = value
    }

    fun handlePopExceptionHandling() {
        exceptionHandlers.removeLast()
    }

    fun handlePop() {
        stack.removeLast()
    }

    fun handlePopLoopHandling() {
        loopHandlers.removeLast()
    }

    fun handlePopScope() {
        currentScope = currentScope.parent ?: throw error("Can't pop root scope.")
    }

    fun handlePushBoolean(value: Boolean) {
        stack.add(if (value) LTrue else LFalse)
    }

    fun handlePushDecimal(immediateValue: Int) {
        stack.add(LDecimal(immediateValue.toDouble()))
    }

    fun handlePushExceptionHandling(catchLabel: Int, endLabel: Int) {
        exceptionHandlers.add(ExceptionHandler(stack.size, catchLabel, endLabel))
    }

    fun handlePushInteger(immediateValue: Int) {
        stack.add(LInteger(immediateValue.toLong()))
    }

    fun handlePushLoopHandling(breakLabel: Int, continueLabel: Int) {
        loopHandlers.add(LoopHandler(stack.size, breakLabel, continueLabel))
    }

    fun handlePushNull() {
        stack.add(LNull)
    }

    fun handlePushScope() {
        currentScope = DefaultMutableScope(currentScope)
    }

    fun handlePushThis() {
        stack.add(currentThis ?: error("There's no 'this' defined."))
    }

    fun handleReturn() {
        TODO("Not yet implemented")
    }

    fun handleSetMemberProperty(nameConst: Int) {
        TODO("Not yet implemented")
    }

    fun handleSetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    fun handleSetVariable(nameConst: Int) {
        currentScope.set(currentSource.stringPool[nameConst], stack.removeLast())
    }

    fun handleThrow() {
        TODO("Not yet implemented")
    }

    fun handleTypeof() {
        stack.add(LString(stack.removeLast().linType))
    }

    fun handleUnaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }

    data class ExceptionHandler(val keepOnStack: Int, val jumpOnException: Int, val jumpOnEnd: Int)
    data class LoopHandler(val keepOnStack: Int, val jumpOnBreak: Int, val jumpOnContinue: Int)
}
