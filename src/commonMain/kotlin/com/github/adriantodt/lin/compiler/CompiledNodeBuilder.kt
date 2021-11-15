package com.github.adriantodt.lin.compiler

import com.github.adriantodt.lin.bytecode.CompiledNode
import com.github.adriantodt.lin.bytecode.CompiledParameter
import com.github.adriantodt.lin.bytecode.JumpLabel
import com.github.adriantodt.lin.bytecode.SectionLabel
import com.github.adriantodt.lin.bytecode.insn.*
import com.github.adriantodt.lin.utils.BinaryOperationType
import com.github.adriantodt.lin.utils.UnaryOperationType
import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.tartar.api.lexer.Sectional

public class CompiledNodeBuilder(private val parent: CompiledSourceBuilder, public val nodeId: Int) {
    private val instructions = mutableListOf<Insn>()

    private val jumpLabels = mutableListOf<JumpLabel>()

    private val sectionLabels = mutableListOf<SectionLabel>()
    private val sectionStack = mutableListOf<Int>()
    private var lastSectionInsn = 0

    private var nextLabelCode = 0

    public fun nextLabel(): Int {
        return nextLabelCode++
    }

    /**
     * Creates an array.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (array)
     */
    public fun newArrayInsn() {
        instructions += NewArrayInsn
    }

    /**
     * Pushes a value into the array.
     *
     * Stack Inputs: (array, value)
     *
     * Stack Outputs: (array)
     */
    public fun arrayInsertInsn() {
        instructions += ArrayInsertInsn
    }

    /**
     * Assigns a value to a variable.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    public fun assignInsn(name: String) {
        instructions += AssignInsn(parent.constantId(name))
    }

    /**
     * Pushes a boolean into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    public fun pushBooleanInsn(value: Boolean) {
        instructions += PushBooleanInsn(value)
    }

    /**
     * Pushes a double into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    public fun pushDecimalInsn(value: Double) {
        if (value % 1 == 0.0 && value.toInt() in i24Range) {
            instructions += PushDecimalInsn(value.toInt())
            return
        }
        instructions += LoadDecimalInsn(parent.constantId(value))
    }

    /**
     * Pushes a long into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    public fun pushIntegerInsn(value: Long) {
        if (value % 1 == 0L && value.toInt() in i24Range) {
            instructions += PushIntegerInsn(value.toInt())
            return
        }
        instructions += LoadIntegerInsn(parent.constantId(value))
    }

    /**
     * Invokes a function on the stack with arguments.
     *
     * Stack Inputs: (function, args...)
     *
     * Stack Outputs: (result)
     */
    public fun invokeInsn(size: Int) {
        instructions += InvokeInsn(size)
    }

    /**
     * Loads a function from the scope and invokes it with arguments.
     *
     * Stack Inputs: (args...)
     *
     * Stack Outputs: (result)
     */
    public fun invokeLocalInsn(name: String, size: Int) {
        instructions += InvokeLocalInsn(parent.constantId(name), size)
    }

    /**
     * Invokes a member function from an object on the stack with arguments.
     *
     * Stack Inputs: (object, args...)
     *
     * Stack Outputs: (result)
     */
    public fun invokeMemberInsn(name: String, size: Int) {
        instructions += InvokeMemberInsn(parent.constantId(name), size)
    }

    /**
     * Pushes a string into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    public fun pushStringInsn(value: String) {
        if (value.length <= 1) {
            instructions += PushCharInsn(value.firstOrNull() ?: (-1).toChar())
            return
        }
        instructions += LoadStringInsn(parent.constantId(value))
    }

    /**
     * Returns the value on the top of the stack.
     *
     * Stack Inputs: (value)
     */
    public fun returnInsn() {
        instructions += ReturnInsn
    }

    /**
     * Pushes `this` into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (this)
     */
    public fun pushThisInsn() {
        instructions += PushThisInsn
    }

    /**
     * Pushes `null` into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (null)
     */
    public fun pushNullInsn() {
        instructions += PushNullInsn
    }

    /**
     * Pushes a string representing the type of the value on the top of the stack.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: (type of value)
     */
    public fun typeofInsn() {
        instructions += TypeofInsn
    }

    /**
     * Creates an object.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (array)
     */
    public fun newObjectInsn() {
        instructions += NewObjectInsn
    }

    /**
     * Pushes an entry into the object.
     *
     * Stack Inputs: (object, key, value)
     *
     * Stack Outputs: (object)
     */
    public fun objectInsertInsn() {
        instructions += ObjectInsertInsn
    }

    /**
     * Breaks a loop.
     *
     * Stack Inputs: ()
     */
    public fun breakInsn() {
        instructions += BreakInsn
    }

    /**
     * Continues a loop.
     *
     * Stack Inputs: ()
     */
    public fun continueInsn() {
        instructions += ContinueInsn
    }

    /**
     * Throws the object from the top of the stack.
     *
     * Stack Inputs: (value)
     */
    public fun throwInsn() {
        instructions += ThrowInsn
    }

    /**
     * Jumps to the label specified.
     */
    public fun jumpInsn(labelCode: Int) {
        instructions += JumpInsn(labelCode)
    }

    /**
     * Branches to the label if the top value of the stack is false (according to the truth rules).
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    public fun branchIfFalseInsn(labelCode: Int) {
        instructions += BranchIfInsn(false, labelCode)
    }

    /**
     * Branches to the label if the top value of the stack is true (according to the truth rules).
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    public fun branchIfTrueInsn(labelCode: Int) {
        instructions += BranchIfInsn(true, labelCode)
    }

    public fun unaryOperationInsn(operator: UnaryOperationType) {
        instructions += when (operator) {
            UnaryOperationType.POSITIVE -> UnaryPositiveOperationInsn
            UnaryOperationType.NEGATIVE -> UnaryNegativeOperationInsn
            UnaryOperationType.NOT -> UnaryNotOperationInsn
            UnaryOperationType.TRUTH -> UnaryTruthOperationInsn
        }
    }

    public fun binaryOperationInsn(operator: BinaryOperationType) {
        instructions += when (operator) {
            BinaryOperationType.ADD -> BinaryAddOperationInsn
            BinaryOperationType.SUBTRACT -> BinarySubtractOperationInsn
            BinaryOperationType.MULTIPLY -> BinaryMultiplyOperationInsn
            BinaryOperationType.DIVIDE -> BinaryDivideOperationInsn
            BinaryOperationType.REMAINING -> BinaryRemainingOperationInsn
            BinaryOperationType.EQUALS -> BinaryEqualsOperationInsn
            BinaryOperationType.NOT_EQUALS -> BinaryNotEqualsOperationInsn
            BinaryOperationType.LT -> BinaryLtOperationInsn
            BinaryOperationType.LTE -> BinaryLteOperationInsn
            BinaryOperationType.GT -> BinaryGtOperationInsn
            BinaryOperationType.GTE -> BinaryGteOperationInsn
            BinaryOperationType.IN -> BinaryInOperationInsn
            BinaryOperationType.RANGE -> BinaryRangeOperationInsn
            else -> throw RuntimeException("The operator $operator can't be converted to a instruction and must be de-sugared.")
        }
    }

    public fun declareVariableInsn(name: String, mutable: Boolean) {
        instructions += DeclareVariableInsn(parent.constantId(name), mutable)
    }

    /**
     * Loads a variable from the scope into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    public fun getVariableInsn(name: String) {
        instructions += GetVariableInsn(parent.constantId(name))
    }

    public fun setVariableInsn(name: String) {
        instructions += SetVariableInsn(parent.constantId(name))
    }

    public fun getMemberPropertyInsn(name: String) {
        instructions += GetMemberPropertyInsn(parent.constantId(name))
    }

    public fun setMemberPropertyInsn(name: String) {
        instructions += SetMemberPropertyInsn(parent.constantId(name))
    }

    public fun getSubscriptInsn(size: Int) {
        instructions += GetSubscriptInsn(size)
    }

    public fun setSubscriptInsn(size: Int) {
        instructions += SetSubscriptInsn(size)
    }

    public fun newFunctionInsn(parameters: List<CompiledParameter>, name: String?, bodyId: Int, varargsParam: Int) {
        instructions += NewFunctionInsn(
            parent.registerFunction(
                parent.registerParameters(parameters),
                name,
                bodyId,
                varargsParam
            )
        )
    }

    public fun dupInsn() {
        instructions += DupInsn
    }

    public fun popInsn() {
        instructions += PopInsn
    }

    public fun pushScopeInsn() {
        instructions += PushScopeInsn
    }

    public fun popScopeInsn() {
        instructions += PopScopeInsn
    }

    public fun pushExceptionHandlingInsn(catchLabel: Int, endLabel: Int) {
        instructions += PushExceptionHandlingInsn(catchLabel, endLabel)
    }

    public fun popExceptionHandlingInsn() {
        instructions += PopExceptionHandlingInsn
    }

    public fun pushLoopHandlingInsn(continueLabel: Int, breakLabel: Int) {
        instructions += PushLoopHandlingInsn(continueLabel, breakLabel)
    }

    public fun popLoopHandlingInsn() {
        instructions += PopLoopHandlingInsn
    }

    /**
     * Marks a label.
     *
     * This does not produce an instruction, but rather a label.
     */
    public fun markLabel(code: Int) {
        jumpLabels += JumpLabel(at = instructions.size, code = code)
    }

    public fun markSectionStart(sectionId: Int) {
        val last = sectionStack.lastOrNull()
        sectionStack.add(sectionId)
        if (last != null) generateSectionLabel(last)
    }

    public fun markSectionStart(section: Section) {
        markSectionStart(parent.sectionId(section))
    }

    public fun markSectionEnd() {
        val last = sectionStack.removeLast()
        generateSectionLabel(last)
    }

    public inline fun markSection(sectionId: Int, block: () -> Unit) {
        markSectionStart(sectionId)
        block()
        markSectionEnd()
    }

    public inline fun markSection(sectional: Sectional, block: () -> Unit) {
        val section = sectional.section ?: return block()
        markSectionStart(section)
        block()
        markSectionEnd()
    }

    /**
     * Automatically pushes/pops the required exception handlers.
     */
    public inline fun withExceptionHandling(catchLabel: Int, endLabel: Int, block: () -> Unit) {
        pushExceptionHandlingInsn(catchLabel, endLabel)
        block()
        popExceptionHandlingInsn()
    }

    /**
     * Automatically pushes/pops the required loop handlers.
     */
    public inline fun withLoopHandling(continueLabel: Int, breakLabel: Int, block: () -> Unit) {
        pushLoopHandlingInsn(continueLabel, breakLabel)
        block()
        popLoopHandlingInsn()
    }

    /**
     * Automatically pushes/pops the required scope instructions.
     */
    public inline fun withScope(block: () -> Unit) {
        pushScopeInsn()
        block()
        popScopeInsn()
    }

    private fun generateSectionLabel(lastSectionId: Int) {
        val currSectionInsn = instructions.size
        if (lastSectionInsn < currSectionInsn) {
            val length = currSectionInsn - lastSectionInsn
            sectionLabels.add(SectionLabel(length, lastSectionId))
            lastSectionInsn = currSectionInsn
        }
    }

    public fun build(): CompiledNode {
        if (sectionStack.isNotEmpty()) {
            println("This should not have happened.")
            generateSectionLabel(sectionStack.last())
        }
        return CompiledNode(instructions.toList(), jumpLabels.toList(), sectionLabels.toList())
    }

    public companion object {
        private const val I24_MAX = 0x7FFFFF
        private const val I24_MIN = -0x800000
        private val i24Range = I24_MIN..I24_MAX
    }
}
