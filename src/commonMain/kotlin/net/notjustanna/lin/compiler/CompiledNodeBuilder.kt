package net.notjustanna.lin.compiler

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledParameter
import net.notjustanna.lin.bytecode.JumpLabel
import net.notjustanna.lin.bytecode.SectionLabel
import net.notjustanna.lin.bytecode.insn.*
import net.notjustanna.lin.utils.BinaryOperationType
import net.notjustanna.lin.utils.UnaryOperationType
import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional

class CompiledNodeBuilder(private val parent: CompiledSourceBuilder, val nodeId: Int) {
    private val instructions = mutableListOf<Insn>()

    private val jumpLabels = mutableListOf<JumpLabel>()

    private val sectionLabels = mutableListOf<SectionLabel>()
    private val sectionStack = mutableListOf<Int>()
    private var lastSectionInsn = 0


    private var nextLabelCode = 0

    fun nextLabel(): Int {
        return nextLabelCode++
    }

    /**
     * Creates an array.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (array)
     */
    fun newArrayInsn() {
        instructions += NewArrayInsn
    }

    /**
     * Pushes a value into the array.
     *
     * Stack Inputs: (array, value)
     *
     * Stack Outputs: (array)
     */
    fun arrayInsertInsn() {
        instructions += ArrayInsertInsn
    }

    /**
     * Assigns a value to a variable.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    fun assignInsn(name: String) {
        instructions += AssignInsn(parent.constantId(name))
    }

    /**
     * Pushes a boolean into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushBooleanInsn(value: Boolean) {
        instructions += PushBooleanInsn(value)
    }

    /**
     * Pushes a double into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushDecimalInsn(value: Double) {
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
    fun pushIntegerInsn(value: Long) {
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
    fun invokeInsn(size: Int) {
        instructions += InvokeInsn(size)
    }

    /**
     * Loads a function from the scope and invokes it with arguments.
     *
     * Stack Inputs: (args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeLocalInsn(name: String, size: Int) {
        instructions += InvokeLocalInsn(parent.constantId(name), size)
    }

    /**
     * Invokes a member function from an object on the stack with arguments.
     *
     * Stack Inputs: (object, args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeMemberInsn(name: String, size: Int) {
        instructions += InvokeMemberInsn(parent.constantId(name), size)
    }

    /**
     * Pushes a string into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushStringInsn(value: String) {
        instructions += LoadStringInsn(parent.constantId(value))
    }

    /**
     * Returns the value on the top of the stack.
     *
     * Stack Inputs: (value)
     */
    fun returnInsn() {
        instructions += ReturnInsn
    }

    /**
     * Pushes `this` into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (this)
     */
    fun pushThisInsn() {
        instructions += PushThisInsn
    }

    /**
     * Pushes `null` into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (null)
     */
    fun pushNullInsn() {
        instructions += PushNullInsn
    }

    /**
     * Pushes a string representing the type of the value on the top of the stack.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: (type of value)
     */
    fun typeofInsn() {
        instructions += TypeofInsn
    }

    /**
     * Creates an object.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (array)
     */
    fun newObjectInsn() {
        instructions += NewObjectInsn
    }

    /**
     * Pushes an entry into the object.
     *
     * Stack Inputs: (object, key, value)
     *
     * Stack Outputs: (object)
     */
    fun objectInsertInsn() {
        instructions += ObjectInsertInsn
    }

    /**
     * Breaks a loop.
     *
     * Stack Inputs: ()
     */
    fun breakInsn() {
        instructions += BreakInsn
    }

    /**
     * Continues a loop.
     *
     * Stack Inputs: ()
     */
    fun continueInsn() {
        instructions += ContinueInsn
    }

    /**
     * Throws the object from the top of the stack.
     *
     * Stack Inputs: (value)
     */
    fun throwInsn() {
        instructions += ThrowInsn
    }

    /**
     * Jumps to the label specified.
     */
    fun jumpInsn(labelCode: Int) {
        instructions += JumpInsn(labelCode)
    }

    /**
     * Branches to the label if the top value of the stack is false (according to the truth rules).
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    fun branchIfFalseInsn(labelCode: Int) {
        instructions += BranchIfInsn(false, labelCode)
    }

    /**
     * Branches to the label if the top value of the stack is true (according to the truth rules).
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    fun branchIfTrueInsn(labelCode: Int) {
        instructions += BranchIfInsn(true, labelCode)
    }

    fun unaryOperationInsn(operator: UnaryOperationType) {
        instructions += when (operator) {
            UnaryOperationType.POSITIVE -> UnaryPositiveOperationInsn
            UnaryOperationType.NEGATIVE -> UnaryNegativeOperationInsn
            UnaryOperationType.NOT -> UnaryNotOperationInsn
            UnaryOperationType.TRUTH -> UnaryTruthOperationInsn
        }
    }

    fun binaryOperationInsn(operator: BinaryOperationType) {
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

    fun declareVariableInsn(name: String, mutable: Boolean) {
        instructions += DeclareVariableInsn(parent.constantId(name), mutable)
    }

    /**
     * Loads a variable from the scope into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun getVariableInsn(name: String) {
        instructions += GetVariableInsn(parent.constantId(name))
    }

    fun setVariableInsn(name: String) {
        instructions += SetVariableInsn(parent.constantId(name))
    }

    fun getMemberPropertyInsn(name: String) {
        instructions += GetMemberPropertyInsn(parent.constantId(name))
    }

    fun setMemberPropertyInsn(name: String) {
        instructions += SetMemberPropertyInsn(parent.constantId(name))
    }

    fun getSubscriptInsn(size: Int) {
        instructions += GetSubscriptInsn(size)
    }

    fun setSubscriptInsn(size: Int) {
        instructions += SetSubscriptInsn(size)
    }

    fun newFunctionInsn(parameters: List<CompiledParameter>, name: String?, bodyId: Int) {
        instructions += NewFunctionInsn(
            parent.registerFunction(
                parent.registerParameters(parameters),
                name,
                bodyId
            )
        )
    }

    fun dupInsn() {
        instructions += DupInsn
    }

    fun popInsn() {
        instructions += PopInsn
    }

    fun pushScopeInsn() {
        instructions += PushScopeInsn
    }

    fun popScopeInsn() {
        instructions += PopScopeInsn
    }

    fun pushExceptionHandlingInsn(catchLabel: Int, endLabel: Int) {
        instructions += PushExceptionHandlingInsn(catchLabel, endLabel)
    }

    fun popExceptionHandlingInsn() {
        instructions += PopExceptionHandlingInsn
    }

    fun pushLoopHandlingInsn(continueLabel: Int, breakLabel: Int) {
        instructions += PushLoopHandlingInsn(continueLabel, breakLabel)
    }

    fun popLoopHandlingInsn() {
        instructions += PopLoopHandlingInsn
    }

    /**
     * Marks a label.
     *
     * This does not produce an instruction, but rather a label.
     */
    fun markLabel(code: Int) {
        jumpLabels += JumpLabel(at = instructions.size, code = code)
    }

    fun markSectionStart(sectionId: Int) {
        val last = sectionStack.lastOrNull()
        sectionStack.add(sectionId)
        if (last != null) generateSectionLabel(last)
    }

    fun markSectionStart(section: Section) {
        markSectionStart(parent.sectionId(section))
    }

    fun markSectionEnd() {
        val last = sectionStack.removeLast()
        generateSectionLabel(last)
    }

    inline fun markSection(sectionId: Int, block: () -> Unit) {
        markSectionStart(sectionId)
        block()
        markSectionEnd()
    }

    inline fun markSection(sectional: Sectional, block: () -> Unit) {
        val section = sectional.section ?: return block()
        markSectionStart(section)
        block()
        markSectionEnd()
    }

    /**
     * Automatically pushes/pops the required exception handlers.
     */
    inline fun withExceptionHandling(catchLabel: Int, endLabel: Int, block: () -> Unit) {
        pushExceptionHandlingInsn(catchLabel, endLabel)
        block()
        popExceptionHandlingInsn()
    }

    /**
     * Automatically pushes/pops the required loop handlers.
     */
    inline fun withLoopHandling(continueLabel: Int, breakLabel: Int, block: () -> Unit) {
        pushLoopHandlingInsn(continueLabel, breakLabel)
        block()
        popLoopHandlingInsn()
    }

    /**
     * Automatically pushes/pops the required scope instructions.
     */
    inline fun withScope(block: () -> Unit) {
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

    fun build(): CompiledNode {
        if (sectionStack.isNotEmpty()) {
            println("This should not have happened.")
            generateSectionLabel(sectionStack.last())
        }
        return CompiledNode(instructions.toList(), jumpLabels.toList(), sectionLabels.toList())
    }

    companion object {
        private const val I24_MAX = 0x7FFFFF
        private const val I24_MIN = -0x800000
        private val i24Range = I24_MIN..I24_MAX
    }
}
