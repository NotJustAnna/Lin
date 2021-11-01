package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.vm.types.LAny

class LinVM(source: CompiledSource) {
    val sources = mutableListOf(source)
    val shelvedContexts = mutableListOf<ExecutionContext>()

    var currentSource = source
    var currentNode = currentSource.nodes[0]
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
        TODO("Not yet implemented")
    }

    private fun handleAssign(nameConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleBinaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
    }

    private fun handleBranchIf(value: Boolean, labelCode: Int) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    private fun handleDup() {
        TODO("Not yet implemented")
    }

    private fun handleGetMemberProperty(nameConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleGetSubscript(size: Int) {
        TODO("Not yet implemented")
    }

    private fun handleGetVariable(nameConst: Int) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    private fun handleLoadDecimal(valueConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleLoadInteger(valueConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleLoadString(valueConst: Int) {
        TODO("Not yet implemented")
    }

    private fun handleNewArray() {
        TODO("Not yet implemented")
    }

    private fun handleNewFunction(functionId: Int) {
        TODO("Not yet implemented")
    }

    private fun handleNewObject() {
        TODO("Not yet implemented")
    }

    private fun handleObjectInsert() {
        TODO("Not yet implemented")
    }

    private fun handlePopExceptionHandling() {
        TODO("Not yet implemented")
    }

    private fun handlePop() {
        TODO("Not yet implemented")
    }

    private fun handlePopLoopHandling() {
        TODO("Not yet implemented")
    }

    private fun handlePopScope() {
        TODO("Not yet implemented")
    }

    private fun handlePushBoolean(value: Boolean) {
        TODO("Not yet implemented")
    }

    private fun handlePushDecimal(immediateValue: Int) {
        TODO("Not yet implemented")
    }

    private fun handlePushExceptionHandling(catchLabel: Int, endLabel: Int) {
        TODO("Not yet implemented")
    }

    private fun handlePushInteger(immediateValue: Int) {
        TODO("Not yet implemented")
    }

    private fun handlePushLoopHandling(breakLabel: Int, continueLabel: Int) {
        TODO("Not yet implemented")
    }

    private fun handlePushNull() {
        TODO("Not yet implemented")
    }

    private fun handlePushScope() {
        TODO("Not yet implemented")
    }

    private fun handlePushThis() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    private fun handleThrow() {
        TODO("Not yet implemented")
    }

    private fun handleTypeof() {
        TODO("Not yet implemented")
    }

    private fun handleUnaryOperation(operatorId: Int) {
        TODO("Not yet implemented")
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
