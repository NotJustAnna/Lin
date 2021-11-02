package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.LAny

class LinVM(source: CompiledSource, node: CompiledNode = source.nodes[0], rootScope: Scope? = null) : InsnEvents {
    /**
     * Contexts below the current context.
     */
    var next: Int = 0

    //    val shelvedContexts = mutableListOf<ExecutionContext>()
    var handleFinish: (LinVM.() -> Unit)? = null
    var result: NodeResult? = null

    var currentNode = node

//    data class ExecutionContext(
//        var scope: Scope,
//        var source: CompiledSource,
//        var node: CompiledNode,
//        var thisValue: LAny?,
//        var next: Int,
//        val stack: MutableList<LAny>,
//        val exceptionHandlers: MutableList<InsnHandler.ExceptionHandler>,
//        val loopHandlers: MutableList<InsnHandler.LoopHandler>
//    )

//    fun shelveContext() {
//        shelvedContexts += ExecutionContext(
//            currentScope, currentSource, currentNode, currentThis, next, stack, exceptionHandlers, loopHandlers
//        )
//    }

//    fun unshelveContext() {
//        val last = shelvedContexts.removeLast()
//        this.currentScope = last.scope
//        this.currentSource = last.source
//        this.currentNode = last.node
//        this.currentThis = last.thisValue
//        this.next = last.next
//        this.stack = last.stack
//        this.exceptionHandlers = last.exceptionHandlers
//        this.loopHandlers = last.loopHandlers
//    }

    val h = InsnHandler(DefaultMutableScope(rootScope), source, this)

    fun step(): Boolean {
        if (result != null || next > currentNode.instructions.lastIndex) {
            val h = handleFinish
            handleFinish = null
            if (h != null) this.h()
            return result != null || handleFinish != null
        }
        when (val insn = currentNode.instructions[next++]) {
            ArrayInsertInsn -> h.handleArrayInsert()
            is AssignInsn -> h.handleAssign(insn.nameConst)
            is BinaryOperationInsn -> h.handleBinaryOperation(insn.operatorId)
            is BranchIfInsn -> h.handleBranchIf(insn.value, insn.labelCode)
            BreakInsn -> h.handleBreak()
            CheckNotNullInsn -> h.handleCheckNotNull()
            ContinueInsn -> h.handleContinue()
            is DeclareVariableInsn -> h.handleDeclareVariable(insn.mutable, insn.nameConst)
            DupInsn -> h.handleDup()
            is GetMemberPropertyInsn -> h.handleGetMemberProperty(insn.nameConst)
            is GetSubscriptInsn -> h.handleGetSubscript(insn.size)
            is GetVariableInsn -> h.handleGetVariable(insn.nameConst)
            is InvokeInsn -> h.handleInvoke(insn.size)
            is InvokeLocalInsn -> h.handleInvokeLocal(insn.nameConst, insn.size)
            is InvokeMemberInsn -> h.handleInvokeMember(insn.nameConst, insn.size)
            is JumpInsn -> h.handleJump(insn.labelCode)
            is LoadDecimalInsn -> h.handleLoadDecimal(insn.valueConst)
            is LoadIntegerInsn -> h.handleLoadInteger(insn.valueConst)
            is LoadStringInsn -> h.handleLoadString(insn.valueConst)
            NewArrayInsn -> h.handleNewArray()
            is NewFunctionInsn -> h.handleNewFunction(insn.functionId)
            NewObjectInsn -> h.handleNewObject()
            ObjectInsertInsn -> h.handleObjectInsert()
            PopExceptionHandlingInsn -> h.handlePopExceptionHandling()
            PopInsn -> h.handlePop()
            PopLoopHandlingInsn -> h.handlePopLoopHandling()
            PopScopeInsn -> h.handlePopScope()
            is PushBooleanInsn -> h.handlePushBoolean(insn.value)
            is PushDecimalInsn -> h.handlePushDecimal(insn.immediateValue)
            is PushExceptionHandlingInsn -> h.handlePushExceptionHandling(insn.catchLabel, insn.endLabel)
            is PushIntegerInsn -> h.handlePushInteger(insn.immediateValue)
            is PushLoopHandlingInsn -> h.handlePushLoopHandling(insn.breakLabel, insn.continueLabel)
            PushNullInsn -> h.handlePushNull()
            PushScopeInsn -> h.handlePushScope()
            PushThisInsn -> h.handlePushThis()
            ReturnInsn -> h.handleReturn()
            is SetMemberPropertyInsn -> h.handleSetMemberProperty(insn.nameConst)
            is SetSubscriptInsn -> h.handleSetSubscript(insn.size)
            is SetVariableInsn -> h.handleSetVariable(insn.nameConst)
            ThrowInsn -> h.handleThrow()
            TypeofInsn -> h.handleTypeof()
            is UnaryOperationInsn -> h.handleUnaryOperation(insn.operatorId)
        }
        return true
    }

//    fun configureCompiledFunction(thisValue: LAny?, function: LFunction.Compiled, arguments: List<LAny>) {
//        // TODO Configure arguments.
//        configure(
//            function.source,
//            function.source.nodes[function.data.bodyId],
//            DefaultMutableScope(function.rootScope),
//            thisValue
//        )
//        this.handleFinish = {
//            val r = result
//            unshelveContext()
//            result = null
//            when (r) {
//                is NodeResult.Returned -> stack.add(r.value)
//                is NodeResult.Thrown -> TODO()
//                null -> stack.add(LNull)
//            }
//        }
//    }

//    private fun configure(source: CompiledSource, node: CompiledNode, scope: Scope, thisValue: LAny?) {
//        this.currentSource = source
//        this.currentNode = node
//        this.currentScope = scope
//        this.currentThis = thisValue
//        this.next = 0
//        this.stack = mutableListOf()
//        this.exceptionHandlers = mutableListOf()
//        this.loopHandlers = mutableListOf()
//    }

    sealed class NodeResult {
        data class Returned(val value: LAny) : NodeResult()
        data class Thrown(val value: LAny) : NodeResult()
    }

    override fun jumpToLabel(labelCode: Int) {
        next = resolveLabel(labelCode)
    }

    private fun resolveLabel(code: Int): Int {
        val indexOf = currentNode.labels.binarySearchBy(code) { it.code }
        check(indexOf >= 0) { "Label $code was not found." }
        return currentNode.labels[indexOf].at
    }

}
