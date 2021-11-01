package com.github.adriantodt.lin.compiler

import com.github.adriantodt.lin.bytecode.Label
import com.github.adriantodt.lin.bytecode.insn.*
import com.github.adriantodt.lin.utils.BinaryOperationType
import com.github.adriantodt.lin.utils.UnaryOperationType
import com.github.adriantodt.tartar.api.lexer.Sectional

class CompiledNodeBuilder(val nodeId: Int) {
    var instructions = mutableListOf<Insn>()

    private var labels = mutableListOf<Label>()

    private var functionParameters = mutableListOf<List<CompiledParameter>>()

    private var functions = mutableListOf<CompiledFunction>()

    private var nextLabelCode = 0

//    fun consuming(block: () -> Unit): MutableList<Insn> {
//        stack.add(instructions)
//        instructions = mutableListOf()
//        block()
//        val last = instructions
//        instructions = stack.removeLast()
//        return last
//    }

//    fun addAll(instructions: MutableList<Insn>) {
//        this.instructions += instructions
//    }

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
        instructions += AssignInsn(name)
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
     * Pushes a character into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushCharInsn(value: Char) {
        instructions += PushCharInsn(value)
    }

    /**
     * Pushes a double into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushDoubleInsn(value: Double) {
        instructions += PushDoubleInsn(value)
    }

    /**
     * Pushes a float into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushFloatInsn(value: Float) {
        instructions += PushFloatInsn(value)
    }

    /**
     * Pushes an integer into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushIntInsn(value: Int) {
        instructions += PushIntInsn(value)
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
        instructions += InvokeLocalInsn(name, size)
    }

    /**
     * Invokes a member function from an object on the stack with arguments.
     *
     * Stack Inputs: (object, args...)
     *
     * Stack Outputs: (result)
     */
    fun invokeMemberInsn(name: String, size: Int) {
        instructions += InvokeMemberInsn(name, size)
    }

    /**
     * Pushes a long into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushLongInsn(value: Long) {
        instructions += PushLongInsn(value)
    }

    /**
     * Pushes a string into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun pushStringInsn(value: String) {
        instructions += PushStringInsn(value)
    }

    /**
     * Throws if the value on the top of the stack is null.
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: (value)
     */
    fun checkNotNullInsn() {
        instructions += CheckNotNullInsn
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
        instructions += BranchIfFalseInsn(labelCode)
    }

    /**
     * Branches to the label if the top value of the stack is true (according to the truth rules).
     *
     * Stack Inputs: (value)
     *
     * Stack Outputs: ()
     */
    fun branchIfTrueInsn(labelCode: Int) {
        instructions += BranchIfTrueInsn(labelCode)
    }

    fun unaryOperationInsn(operator: UnaryOperationType) {
        instructions += UnaryOperationInsn(operator)
    }

    fun binaryOperationInsn(operator: BinaryOperationType) {
        instructions += BinaryOperationInsn(operator)
    }

    fun declareVariableInsn(name: String, mutable: Boolean) {
        instructions += DeclareVariableInsn(name, mutable)
    }

    /**
     * Loads a variable from the scope into the stack.
     *
     * Stack Inputs: ()
     *
     * Stack Outputs: (value)
     */
    fun getVariableInsn(name: String) {
        instructions += GetVariableInsn(name)
    }

    fun setVariableInsn(name: String) {
        instructions += SetVariableInsn(name)
    }

    fun getMemberPropertyInsn(name: String) {
        instructions += GetMemberPropertyInsn(name)
    }

    fun setMemberPropertyInsn(name: String) {
        instructions += SetMemberPropertyInsn(name)
    }

    fun getSubscriptInsn(size: Int) {
        instructions += GetSubscriptInsn(size)
    }

    fun setSubscriptInsn(size: Int) {
        instructions += SetSubscriptInsn(size)
    }

    fun newFunctionInsn(parameters: List<CompiledParameter>, name: String?, bodyId: Int) {
        val parametersId = functionParameters.size
        functionParameters += parameters
        val functionId = functions.size
        functions += CompiledFunction(parametersId, name, bodyId)
        instructions += NewFunctionInsn(functionId)
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

    fun pushLoopHandlingInsn(breakLabel: Int, continueLabel: Int) {
        instructions += PushLoopHandlingInsn(breakLabel, continueLabel)
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
        labels += Label(at = instructions.size, code = code)
    }

    fun markSection(sectional: Sectional) {
        // NO-OP
        // TODO IMPLEMENT THIS
        // This probably won't be implemented as a instruction
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
    inline fun withLoopHandling(breakLabel: Int, continueLabel: Int, block: () -> Unit) {
        pushLoopHandlingInsn(breakLabel, continueLabel)
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
}
