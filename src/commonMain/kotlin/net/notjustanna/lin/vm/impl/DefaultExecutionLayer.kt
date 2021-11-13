package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.exception.LinNativeException
import net.notjustanna.lin.exception.LinUnsupportedOperationException
import net.notjustanna.lin.exception.StackUnderflowException
import net.notjustanna.lin.vm.LAnyException
import net.notjustanna.lin.vm.StackTrace
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.MutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.*

class DefaultExecutionLayer(
    private val events: VMEvents,
    private var scope: Scope,
    private val source: CompiledSource,
    private val functionName: String,
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
        try {
            when (insn) {
                ArrayInsertInsn -> handleArrayInsert()
                is AssignInsn -> handleAssign(insn.nameConst)
                is BranchIfInsn -> handleBranchIf(insn.value, insn.labelCode)
                BreakInsn -> handleBreak()
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
        } catch (e: Exception) {
            onThrow(
                when (e) {
                    is LAnyException -> e.value
                    is LinNativeException -> Exceptions.toObject(e, events.stackTrace())
                    else -> Exceptions.fromNative(e, events.stackTrace())
                }
            )
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
        if (handler.keepOnStack < stack.size) {
            println("WTF? Stack is missing ${handler.keepOnStack - stack.size} items!! This is probably a bug!")
        } else if (handler.keepOnStack > stack.size) {
            repeat(handler.keepOnStack - stack.size) { popStack() }
        }
        next = handler.jumpOnException
        stack.add(value)
    }

    data class ExceptionHandler(val keepOnStack: Int, val jumpOnException: Int, val jumpOnEnd: Int)
    data class LoopHandler(val keepOnStack: Int, val jumpOnBreak: Int, val jumpOnContinue: Int)

    private val stack = mutableListOf<LAny>()
    private val exceptionHandlers = mutableListOf<ExceptionHandler>()
    private val loopHandlers = mutableListOf<LoopHandler>()

    private fun popStack(): LAny {
        return stack.removeLastOrNull()
            ?: throw StackUnderflowException("Tried to remove an item from the stack, but the stack is empty.")
    }

    private fun peekStack(): LAny {
        return stack.lastOrNull()
            ?: throw StackUnderflowException("Tried to get the last item from the stack, but the stack is empty.")
    }

    private fun handleArrayInsert() {
        val value = popStack()
        val array = peekStack() as? LArray ?: error("Value is not an LArray.")
        array.value.add(value)
    }

    private fun handleAssign(nameConst: Int) {
        scope.set(source.stringConst(nameConst), popStack())
    }

    private fun handleBranchIf(value: Boolean, labelCode: Int) {
        val truth = popStack().truth()

        if (truth == value) {
            next = node.resolveLabel(labelCode)
        }
    }

    private fun handleBreak() {
        val last = loopHandlers.removeLast()
        next = last.jumpOnBreak
    }

    private fun handleContinue() {
        val last = loopHandlers.removeLast()
        next = last.jumpOnContinue
    }

    private fun handleDeclareVariable(mutable: Boolean, nameConst: Int) {
        val s = scope as? MutableScope ?: error("Current scope is not mutable")
        s.declareVariable(source.stringConst(nameConst), mutable)
    }

    private fun handleDup() {
        stack.add(peekStack())
    }

    private fun handleGetMemberProperty(nameConst: Int) {
        val target = popStack()
        val name = source.stringConst(nameConst)
        val member = target.getMember(name)
        if (member != null) {
            stack.add(member)
        }
        onThrow(Exceptions.noElementExists(name, events.stackTrace()))
    }

    private fun handleGetSubscript(size: Int) {
        val arguments = List(size) { popStack() }.reversed()
        val parent = popStack()
        if (parent is LArray && size == 1) {
            val arg = arguments.first()
            if (arg is LInteger) {
                val element = parent.value.getOrNull(arg.value.toInt())
                if (element != null) {
                    stack.add(element)
                    return
                }
            }
        }
        if (parent is LObject && size == 1) {
            val arg = arguments.first()
            val element = parent.value[arg]
            if (element != null) {
                stack.add(element)
                return
            }
        }
        TODO("Not yet implemented: GetSubscript -> $parent$arguments")
    }

    private fun handleGetVariable(nameConst: Int) {
        stack.add(scope.get(source.stringConst(nameConst)))
    }

    private fun handleInvoke(size: Int) {
        val arguments = List(size) { popStack() }.reversed()
        val function = popStack()
        invocation(null, function, arguments)
    }

    private fun handleInvokeLocal(nameConst: Int, size: Int) {
        val arguments = List(size) { popStack() }.reversed()
        val function = scope.get(source.stringConst(nameConst))
        invocation(null, function, arguments)
    }

    private fun handleInvokeMember(nameConst: Int, size: Int) {
        val arguments = List(size) { popStack() }.reversed()
        val parent = popStack()
        val function = parent.getMember(source.stringConst(nameConst)) ?: LNull
        invocation(parent, function, arguments)
    }

    private fun handleJump(labelCode: Int) {
        next = node.resolveLabel(labelCode)
    }

    private fun handleLoadDecimal(valueConst: Int) {
        stack.add(LDecimal(Double.fromBits(source.longConst(valueConst))))
    }

    private fun handleLoadInteger(valueConst: Int) {
        stack.add(LInteger(source.longConst(valueConst)))
    }

    private fun handleLoadString(valueConst: Int) {
        stack.add(LString(source.stringConst(valueConst)))
    }

    private fun handleNewArray() {
        stack.add(LArray())
    }

    private fun handleNewFunction(functionId: Int) {
        val functionData = source.functions[functionId]
        stack.add(LCompiledFunction(source, functionData, scope))
    }

    private fun handleNewObject() {
        stack.add(LObject())
    }

    private fun handleObjectInsert() {
        val value = popStack()
        val key = popStack()
        val obj = peekStack() as? LObject ?: error("Value is not an LObject.")
        obj.value[key] = value
    }

    private fun handlePopExceptionHandling() {
        exceptionHandlers.removeLast()
    }

    private fun handlePop() {
        popStack()
    }

    private fun handlePopLoopHandling() {
        loopHandlers.removeLast()
    }

    private fun handlePopScope() {
        scope = scope.parent ?: error("Can't pop root scope.")
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
        events.onReturn(popStack())
    }

    private fun handleSetMemberProperty(nameConst: Int) {
        val value = popStack()
        val s = source.stringConst(nameConst)
        val parent = popStack()
        if (parent is LObject) {
            parent.value[LString(s)] = value
        }
        TODO("Not yet implemented: SetMember $parent.$s = $value")
    }

    private fun handleSetSubscript(size: Int) {
        val value = popStack()
        val arguments = List(size) { popStack() }.reversed()
        val parent = popStack()
        if (parent is LArray && size == 1) {
            val arg = arguments.first()
            if (arg is LInteger) {
                parent.value[arg.value.toInt()] = value
                return
            }
        }
        if (parent is LObject && size == 1) {
            val arg = arguments.first()
            parent.value[arg] = value
            return
        }
        TODO("Not yet implemented: SetSubscript -> $parent$arguments = $value")
    }

    private fun handleSetVariable(nameConst: Int) {
        scope.set(source.stringConst(nameConst), popStack())
    }

    private fun handleThrow() {
        onThrow(popStack())
    }

    private fun handleTypeof() {
        stack.add(LString(popStack().linType))
    }

    private fun handleBinaryAddOperation() {
        val right = popStack()
        val left = popStack()
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
        throw LinUnsupportedOperationException("add", left.linType, right.linType)
    }

    private fun handleBinaryDivideOperation() {
        val right = popStack()
        val left = popStack()
        if (left is LNumber && right is LNumber) {
            stack.add(left / right)
            return
        }
        throw LinUnsupportedOperationException("divide", left.linType, right.linType)
    }

    private fun handleBinaryEqualsOperation() {
        val right = popStack()
        val left = popStack()
        stack.add(LAny.ofBoolean(right == left))
    }

    private fun handleBinaryMultiplyOperation() {
        val right = popStack()
        val left = popStack()
        if (left is LString && right is LInteger) {
            stack.add(LString(left.value.repeat(right.value.toInt())))
        }
        if (left is LNumber && right is LNumber) {
            stack.add(left * right)
            return
        }
        throw LinUnsupportedOperationException("multiply", left.linType, right.linType)
    }

    private fun handleBinaryNotEqualsOperation() {
        val right = popStack()
        val left = popStack()
        stack.add(LAny.ofBoolean(right != left))
    }

    private fun handleBinaryRangeOperation() {
        val right = popStack()
        val left = popStack()
        if (left is LInteger && right is LInteger) {
            stack.add(left..right)
            return
        }
        throw LinUnsupportedOperationException("range", left.linType, right.linType)
    }

    private fun handleBinaryRemainingOperation() {
        val right = popStack()
        val left = popStack()
        if (left is LNumber && right is LNumber) {
            stack.add(left % right)
            return
        }
        throw LinUnsupportedOperationException("remaining", left.linType, right.linType)
    }

    private fun handleBinarySubtractOperation() {
        val right = popStack()
        val left = popStack()
        if (left is LNumber && right is LNumber) {
            stack.add(left - right)
            return
        }
        throw LinUnsupportedOperationException("subtract", left.linType, right.linType)
    }

    private fun handleBinaryComparison(comparison: Comparison) {
        val right = popStack()
        val left = popStack()
        if (left is LString && right is LString) {
            stack.add(LAny.ofBoolean(comparison.toBoolean(left.value.compareTo(right.value))))
            return
        }
        if (left is LNumber && right is LNumber) {
            stack.add(LAny.ofBoolean(comparison.toBoolean(left.compareTo(right))))
            return
        }
        throw LinUnsupportedOperationException("comparison", left.linType, right.linType)
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
        val right = popStack()
        val left = popStack()
        if (right is LArray) {
            stack.add(LAny.ofBoolean(left in right.value))
            return
        }
        if (right is LObject) {
            stack.add(LAny.ofBoolean(left in right.value))
            return
        }
        throw LinUnsupportedOperationException("in", left.linType, right.linType)
    }

    private fun handleUnaryNegativeOperation() {
        val target = popStack()
        if (target is LNumber) {
            stack.add(-target)
            return
        }
        throw LinUnsupportedOperationException("negative", target.linType)
    }

    private fun handleUnaryNotOperation() {
        stack.add(LAny.ofBoolean(!popStack().truth()))
    }

    private fun handleUnaryPositiveOperation() {
        val target = popStack()
        if (target is LNumber) {
            stack.add(+target)
            return
        }
        throw LinUnsupportedOperationException("positive", target.linType)
    }

    private fun handleUnaryTruthOperation() {
        stack.add(LAny.ofBoolean(popStack().truth()))
    }

    private fun invocation(thisValue: LAny?, function: LAny, args: List<LAny>) {
        when (function) {
            is LNativeFunction -> {
                try {
                    stack.add(function.block(thisValue, args))
                } catch (e: Exception) {
                    val stackTrace = listOf(StackTrace(function.name ?: "<anonymous function>")) + events.stackTrace()
                    onThrow(
                        when (e) {
                            is LAnyException -> e.value
                            is LinNativeException -> Exceptions.toObject(e, stackTrace)
                            else -> Exceptions.fromNative(e, stackTrace)
                        }
                    )
                }
            }
            is LCompiledFunction -> {
                val layer = FunctionSetupLayer(events, function, thisValue, args)
                events.pushLayer(layer)
                layer.step()
            }
            else -> {
                onThrow(Exceptions.notAFunction(function.linType, events.stackTrace()))
            }
        }
    }

    override fun trace(): StackTrace? {
        val section = source.sections.getOrNull(findSectionIndex(next - 1)) ?: return null
        return StackTrace(functionName, source.stringConst(section.nameConst), section.line, section.column)
    }

    private fun findSectionIndex(last: Int): Int {
        var atInsn = 0
        for ((length, index) in node.sectionLabels) {
            if (atInsn + length < last) {
                atInsn += length
                continue
            }
            return index
        }
        return -1
    }
}
