package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.MutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.*

class LinVM(source: CompiledSource, rootScope: Scope? = null) {
    val sources = mutableListOf(source)
    val shelvedContexts = mutableListOf<ExecutionContext>()

    var currentScope: Scope = DefaultMutableScope(rootScope)
    var currentSource = source
    var currentNode = currentSource.nodes[0]
    var currentThis: LAny? = null
    var next: Int = 0
    val stack = mutableListOf<LAny>()
    val exceptionHandlers = mutableListOf<ExceptionHandler>()
    val loopHandlers = mutableListOf<LoopHandler>()

    fun step() {
        when (val insn = currentNode.instructions[next++]) {
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
    }

    private fun handleArrayInsert() {
        val value = stack.removeLast()
        val array = stack.last() as? LArray ?: error("Value is not an LArray.")
        array.value.add(value)
    }

    private fun handleAssign(nameConst: Int) {
        currentScope.set(currentSource.stringPool[nameConst], stack.removeLast())
    }

    private fun handleBinaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }

    private fun handleBranchIf(value: Boolean, labelCode: Int) {
        val truth = stack.removeLast().truth()

        if (truth == value) {
            next = resolveLabel(labelCode)
        }
    }

    private fun handleBreak() {
        TODO("Not yet implemented")
    }

    private fun handleCheckNotNull() {
        TODO("Not yet implemented")
    }

    private fun handleContinue() {
        TODO("Not yet implemented")
    }

    private fun handleDeclareVariable(mutable: Boolean, nameConst: Int) {
        val s = currentScope as? MutableScope ?: error("Current scope is not mutable")
        s.declareVariable(currentSource.stringPool[nameConst], mutable)
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
        stack.add(currentScope.get(currentSource.stringPool[nameConst]))
    }

    private fun handleInvoke(size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleInvokeLocal(nameConst: Int, size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleInvokeMember(nameConst: Int, size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleJump(labelCode: Int) {
        next = resolveLabel(labelCode)

    }

    private fun handleLoadDecimal(valueConst: Int) {
        stack.add(LDecimal(Double.fromBits(currentSource.longPool[valueConst])))
    }

    private fun handleLoadInteger(valueConst: Int) {
        stack.add(LInteger(currentSource.longPool[valueConst]))
    }

    private fun handleLoadString(valueConst: Int) {
        stack.add(LString(currentSource.stringPool[valueConst]))
    }

    private fun handleNewArray() {
        stack.add(LArray())
    }

    private fun handleNewFunction(functionId: Int) {
        TODO("Not yet implemented")
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
        currentScope = currentScope.parent ?: throw error("Can't pop root scope.")
    }

    private fun handlePushBoolean(value: Boolean) {
        stack.add(if (value) LTrue else LFalse)
    }

    private fun handlePushDecimal(immediateValue: Int) {
        stack.add(LDecimal(immediateValue.toDouble()))
    }

    private fun handlePushExceptionHandling(catchLabel: Int, endLabel: Int) {
        exceptionHandlers.add(ExceptionHandler(stack.size, resolveLabel(catchLabel), resolveLabel(endLabel)))
    }

    private fun handlePushInteger(immediateValue: Int) {
        stack.add(LInteger(immediateValue.toLong()))
    }

    private fun handlePushLoopHandling(breakLabel: Int, continueLabel: Int) {
        loopHandlers.add(LoopHandler(stack.size, resolveLabel(breakLabel), resolveLabel(continueLabel)))
    }

    private fun handlePushNull() {
        stack.add(LNull)
    }

    private fun handlePushScope() {
        currentScope = DefaultMutableScope(currentScope)
    }

    private fun handlePushThis() {
        stack.add(currentThis ?: error("There's no 'this' defined."))
    }

    private fun handleReturn() {
        TODO("Not yet implemented")
    }

    private fun handleSetMemberProperty(nameConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleSetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleSetVariable(nameConst: Int) {
        currentScope.set(currentSource.stringPool[nameConst], stack.removeLast())
    }

    private fun handleThrow() {
        TODO("Not yet implemented")
    }

    private fun handleTypeof() {
        val type = when (stack.removeLast()) {
            is LArray -> "array"
            is LDecimal -> "decimal"
            LFalse, LTrue -> "boolean"
            is LInteger -> "integer"
            LNull -> "null"
            is LObject -> "object"
            is LString -> "string"
        }

        stack.add(LString(type))
    }

    private fun handleUnaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }

    private fun resolveLabel(code: Int): Int {
        val indexOf = currentNode.labels.binarySearchBy(code) { it.code }
        check(indexOf >= 0) { "Label $code was not found." }
        return currentNode.labels[indexOf].at
    }

    data class ExecutionContext(
        val source: CompiledSource,
        val node: CompiledNode,
        var next: Int = 0,
        val stack: MutableList<LAny> = mutableListOf(),
        val exceptionHandlers: MutableList<ExceptionHandler> = mutableListOf(),
        val loopHandlers: MutableList<LoopHandler> = mutableListOf()
    )

    data class ExceptionHandler(val keepOnStack: Int, val jumpOnException: Int, val jumpOnEnd: Int)
    data class LoopHandler(val keepOnStack: Int, val jumpOnBreak: Int, val jumpOnContinue: Int)
    sealed class NodeResult {
        data class Returned(val value: LAny) : NodeResult()
        data class Thrown(val value: LAny) : NodeResult()
    }
}
